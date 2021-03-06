package com.android.sdk.net.rxjava2;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.HostConfigProvider;
import com.android.sdk.net.NetContext;
import com.android.sdk.net.core.exception.ApiErrorException;
import com.android.sdk.net.core.exception.ServerErrorException;
import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.core.result.ExceptionFactory;
import com.android.sdk.net.core.result.Result;
import com.android.sdk.net.coroutines.CoroutinesSupportCommonKt;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Function;

public class HttpResultTransformer<Upstream, Downstream, T extends Result<Upstream>> implements ObservableTransformer<T, Downstream>, FlowableTransformer<T, Downstream>, SingleTransformer<T, Downstream> {

    private final boolean mRequireNonNullData;

    private final DataExtractor<Downstream, Upstream> mDataExtractor;

    @Nullable
    private final ExceptionFactory mExceptionFactory;

    public HttpResultTransformer(
            boolean requireNonNullData,
            @NonNull DataExtractor<Downstream, Upstream> dataExtractor,
            @Nullable ExceptionFactory exceptionFactory
    ) {
        mRequireNonNullData = requireNonNullData;
        mDataExtractor = dataExtractor;
        mExceptionFactory = exceptionFactory;
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
                (RxResultPostTransformer<Downstream>) NetContext.get().commonProvider().rxResultPostTransformer();

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
                (RxResultPostTransformer<Downstream>) NetContext.get().commonProvider().rxResultPostTransformer();

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
                (RxResultPostTransformer<Downstream>) NetContext.get().commonProvider().rxResultPostTransformer();

        if (rxResultPostTransformer != null) {
            return downstreamSingle.compose(rxResultPostTransformer);
        } else {
            return downstreamSingle;
        }
    }


    private Downstream processData(Result<Upstream> rResult) {

        NetContext netContext = NetContext.get();
        String flag = netContext.getHostFlagHolder().getFlag(rResult.getClass());
        HostConfigProvider hostConfigProvider = netContext.hostConfigProvider(flag);


        if (!rResult.isSuccess()) {//???????????????????????????
            ApiHandler apiHandler = hostConfigProvider.aipHandler();
            if (apiHandler != null) {
                apiHandler.onApiError(rResult);
            }
            throwAs(createException(rResult, flag, hostConfigProvider));
        }

        if (mRequireNonNullData) {
            //????????????????????????????????????????????????????????????????????????????????????
            if (rResult.getData() == null) {
                throwAs(new ServerErrorException(ServerErrorException.SERVER_NULL_DATA));
            }
        }

        return mDataExtractor.getDataFromHttpResult(rResult);
    }

    private Throwable createException(@NonNull Result<Upstream> rResult, String flag, HostConfigProvider hostConfigProvider) {
        ExceptionFactory exceptionFactory = mExceptionFactory;

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

    @SuppressWarnings("unchecked")
    private <E extends Throwable> void throwAs(Throwable throwable) throws E {
        throw (E) throwable;
    }

    private Throwable transformError(Throwable throwable) {
        return CoroutinesSupportCommonKt.transformHttpException(throwable);
    }

}