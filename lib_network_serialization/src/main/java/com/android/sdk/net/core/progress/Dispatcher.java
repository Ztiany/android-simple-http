package com.android.sdk.net.core.progress;


import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * @author Ztiany
 */
class Dispatcher {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    static void dispatch(@NonNull Runnable runnable) {
        HANDLER.post(runnable);
    }

}