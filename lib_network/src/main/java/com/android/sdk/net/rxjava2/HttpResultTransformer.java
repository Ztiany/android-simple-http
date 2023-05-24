package com.android.sdk.net.rxjava2;


import androidx.annotation.NonNull;

import com.android.sdk.net.HostConfigProvider;
import com.android.sdk.net.NetContext;
import com.android.sdk.net.core.exception.ApiErrorException;
import com.android.sdk.net.core.exception.ServerErrorException;
import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.core.result.ExceptionFactory;
import com.android.sdk.net.core.result.Result;
import com.android.sdk.net.coroutines.CommonInternalKt;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;

public class HttpResultTransformer<Upstream, Downstream, T extends Result<Upstream>>
        implements ObservableTransformer<T, Downstream>, FlowableTransformer<T, Downstream>, SingleTransformer<T, Downstream> {

    private final boolean mRequireNonNullData;

    private final DataExtractor<Downstream, Upstream> mDataExtractor;

    private final String mHostFlag;

    public HttpResultTransformer(
            boolean requireNonNullData,
            @NonNull DataExtractor<Downstream, Upstream> dataExtractor,
            @NonNull String hostFlag
    ) {
        mRequireNonNullData = requireNonNullData;
        mDataExtractor = dataExtractor;
        mHostFlag = hostFlag;
    }

    @NonNull
    @Override
    public Publisher<Downstream> apply(Flowable<T> upstream) {

        Flowable<Downstream> downstreamFlowable = upstream.
                onErrorResumeNext(throwable -> {
                    return Flowable.error(transformError(throwable));
                })
                .map(this::processData);

        @SuppressWarnings("unchecked")
        RxResultPostTransformer<Downstream> rxResultPostTransformer =
                (RxResultPostTransformer<Downstream>) NetContext.get().hostConfigProvider(mHostFlag).rxResultPostTransformer();

        if (rxResultPostTransformer != null) {
            return downstreamFlowable.compose(rxResultPostTransformer);
        } else {
            return downstreamFlowable;
        }
    }

    @NonNull
    @Override
    public ObservableSource<Downstream> apply(Observable<T> upstream) {

        Observable<Downstream> downstreamObservable = upstream
                .onErrorResumeNext(throwable -> {
                    return Observable.error(transformError(throwable));
                })
                .map(this::processData);

        @SuppressWarnings("unchecked")
        RxResultPostTransformer<Downstream> rxResultPostTransformer =
                (RxResultPostTransformer<Downstream>) NetContext.get().hostConfigProvider(mHostFlag).rxResultPostTransformer();

        if (rxResultPostTransformer != null) {
            return downstreamObservable.compose(rxResultPostTransformer);
        } else {
            return downstreamObservable;
        }
    }

    @NonNull
    @Override
    public SingleSource<Downstream> apply(Single<T> upstream) {

        Single<Downstream> downstreamSingle = upstream
                .onErrorResumeNext(throwable -> Single.error(transformError(throwable)))
                .map(this::processData);

        @SuppressWarnings("unchecked")
        RxResultPostTransformer<Downstream> rxResultPostTransformer =
                (RxResultPostTransformer<Downstream>) NetContext.get().hostConfigProvider(mHostFlag).rxResultPostTransformer();

        if (rxResultPostTransformer != null) {
            return downstreamSingle.compose(rxResultPostTransformer);
        } else {
            return downstreamSingle;
        }
    }


    private Downstream processData(Result<Upstream> rResult) {

        NetContext netContext = NetContext.get();
        HostConfigProvider hostConfigProvider = netContext.hostConfigProvider(mHostFlag);

        if (!rResult.isSuccess()) {//检测响应码是否正确
            ApiHandler apiHandler = hostConfigProvider.aipHandler();
            if (apiHandler != null) {
                apiHandler.onApiError(rResult);
            }
            throwAs(createException(rResult, mHostFlag, hostConfigProvider));
        }

        if (mRequireNonNullData) {
            //如果约定必须返回的数据却没有返回数据，则认为是服务器错误
            if (rResult.getData() == null) {
                throwAs(new ServerErrorException(ServerErrorException.SERVER_NULL_DATA));
            }
        }

        return mDataExtractor.getDataFromHttpResult(rResult);
    }

    private Throwable createException(@NonNull Result<Upstream> rResult, String flag, HostConfigProvider hostConfigProvider) {
        ExceptionFactory exceptionFactory = NetContext.get().hostConfigProvider(flag).exceptionFactory();

        if (exceptionFactory == null) {
            exceptionFactory = hostConfigProvider.exceptionFactory();
        }

        if (exceptionFactory != null) {
            Exception exception = exceptionFactory.create(rResult, flag);
            if (exception != null) {
                return exception;
            }
        }

        return new ApiErrorException(rResult.getCode(), rResult.getMessage(), flag);
    }

    private <E extends Throwable> void throwAs(Throwable throwable) throws E {
        throw (E) throwable;
    }

    private Throwable transformError(Throwable throwable) {
        return CommonInternalKt.transformHttpException(mHostFlag, throwable);
    }

}