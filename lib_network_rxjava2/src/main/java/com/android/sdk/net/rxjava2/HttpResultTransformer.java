package com.android.sdk.net.rxjava2;

import static com.android.sdk.net.rxjava2.RxRegistrarKt.rxResultPostTransformer;

import androidx.annotation.NonNull;

import com.android.sdk.net.HostConfig;
import com.android.sdk.net.NetContext;
import com.android.sdk.net.core.config.ErrorListener;
import com.android.sdk.net.core.exception.ApiErrorException;
import com.android.sdk.net.core.exception.ServerErrorException;
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

        RxResultPostTransformer<Downstream> rxResultPostTransformer = getRxResultPostTransformer();
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

        RxResultPostTransformer<Downstream> rxResultPostTransformer = getRxResultPostTransformer();
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

        RxResultPostTransformer<Downstream> rxResultPostTransformer = getRxResultPostTransformer();
        if (rxResultPostTransformer != null) {
            return downstreamSingle.compose(rxResultPostTransformer);
        } else {
            return downstreamSingle;
        }
    }

    private Downstream processData(Result<Upstream> rResult) {
        NetContext netContext = NetContext.get();
        HostConfig hostConfig = netContext.hostConfig(mHostFlag);
        ErrorListener errorListener = hostConfig.errorListener();

        if (!rResult.isSuccess()) {
            ApiErrorException exception = createException(rResult, mHostFlag);
            if (errorListener != null) {
                errorListener.onApiException(exception, mHostFlag);
            }
            throwAs(exception);
        }

        if (mRequireNonNullData) {
            // If the data that must be returned is not returned, it is considered a server error.
            if (rResult.getData() == null) {
                ServerErrorException throwable = new ServerErrorException(ServerErrorException.EMPTY_SERVER_DATA);
                if (errorListener != null) {
                    errorListener.onDataNotReturned(throwable, mHostFlag);
                }
                throwAs(throwable);
            }
        }

        return mDataExtractor.getDataFromHttpResult(rResult);
    }

    private ApiErrorException createException(@NonNull Result<Upstream> result, String flag) {
        return new ApiErrorException(
                result.getCode(),
                result.getMessage(),
                result,
                flag
        );
    }

    private <E extends Throwable> void throwAs(Throwable throwable) throws E {
        throw (E) throwable;
    }

    private Throwable transformError(Throwable throwable) {
        return CommonInternalKt.transformHttpException(mHostFlag, throwable);
    }

    @SuppressWarnings("unchecked")
    private RxResultPostTransformer<Downstream> getRxResultPostTransformer() {
        return (RxResultPostTransformer<Downstream>) rxResultPostTransformer(NetContext.get().hostConfig(mHostFlag));
    }

}