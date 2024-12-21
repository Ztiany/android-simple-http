package com.android.sdk.net.core.progress;


import androidx.annotation.NonNull;

public interface ProgressListener {

    void onProgress(long contentLength, long currentBytes, float percent, boolean isFinish);

    void onLoadFail(@NonNull Exception exception);

}
