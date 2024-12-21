package com.android.sdk.net.core.progress;

import android.os.SystemClock;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import timber.log.Timber;

class ProgressRequestBody extends RequestBody {

    private final ProgressListener mProgressListener;
    private final RequestBody mDelegate;
    private final int mRefreshTime;
    private BufferedSink mBufferedSink;

    ProgressRequestBody(RequestBody delegate, int refreshTime, ProgressListener progressListener) {
        this.mDelegate = delegate;
        mProgressListener = progressListener;
        this.mRefreshTime = refreshTime;
    }

    @Override
    public MediaType contentType() {
        return mDelegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return mDelegate.contentLength();
        } catch (IOException e) {
            Timber.e(e);
        }
        return -1;
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(new CountingSink(sink));
        }
        try {
            mDelegate.writeTo(mBufferedSink);
            mBufferedSink.flush();
        } catch (IOException e) {
            Timber.e(e);
            mProgressListener.onLoadFail(e);
            throw e;
        }
    }

    private final class CountingSink extends ForwardingSink {

        private long totalBytesRead = 0L;
        private long lastRefreshTime = 0L;  //最后一次刷新的时间
        private long mContentLength;

        CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            try {
                super.write(source, byteCount);
            } catch (IOException e) {
                Timber.e(e);
                mProgressListener.onLoadFail(e);
                throw e;
            }
            if (mContentLength == 0) { //避免重复调用 contentLength()
                mContentLength = contentLength();
            }
            totalBytesRead += byteCount;

            long curTime = SystemClock.elapsedRealtime();
            boolean isFinish = totalBytesRead == mContentLength;
            if (curTime - lastRefreshTime >= mRefreshTime || isFinish) {
                mProgressListener.onProgress(totalBytesRead, mContentLength, totalBytesRead * 1.0F / mContentLength, isFinish);
                lastRefreshTime = curTime;
            }
        }
    }

}