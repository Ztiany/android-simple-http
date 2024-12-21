package com.android.sdk.net.core.progress;

import androidx.annotation.NonNull;

public interface UrlProgressListener {

    void onProgress(@NonNull String url, long contentLength, long currentBytes, float percent, boolean isFinish);

    void onError(@NonNull String url, @NonNull Exception exception);

}