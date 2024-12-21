package com.android.sdk.net.core.progress;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author Ztiany
 */
public class ResponseProgressInterceptor implements Interceptor {

    private static final int DEFAULT_REFRESH_TIME = 150;

    private final UrlProgressListener mInterceptorProgressListener;

    private int mRefreshTime = DEFAULT_REFRESH_TIME;

    public ResponseProgressInterceptor(UrlProgressListener interceptorProgressListener, int refreshTime) {
        mInterceptorProgressListener = interceptorProgressListener;
        if (mInterceptorProgressListener == null) {
            throw new NullPointerException();
        }
        setRefreshTime(refreshTime);
    }

    public ResponseProgressInterceptor(UrlProgressListener interceptorProgressListener) {
        this(interceptorProgressListener, DEFAULT_REFRESH_TIME);
    }

    public void setRefreshTime(int refreshTime) {
        mRefreshTime = refreshTime;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        return wrapResponseBody(chain.proceed(chain.request()));
    }

    private Response wrapResponseBody(Response response) {
        if (response == null || response.body() == null) {
            return response;
        }

        final String key = response.request().url().toString();
        return response.newBuilder()
                .body(new ProgressResponseBody(response.body(), mRefreshTime, new ProgressListener() {

                    @Override
                    public void onProgress(long contentLength, long currentBytes, float percent, boolean isFinish) {
                        Dispatcher.dispatch(() -> mInterceptorProgressListener.onProgress(key, contentLength, currentBytes, percent, isFinish));
                    }

                    @Override
                    public void onLoadFail(@NonNull Exception exception) {
                        Dispatcher.dispatch(() -> mInterceptorProgressListener.onError(key, exception));
                    }
                })).build();
    }

}