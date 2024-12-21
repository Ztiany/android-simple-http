package com.android.sdk.net.rxjava2;

public class RxJavaChecker {

    private static Boolean mHasRxJava2 = null;

    public static boolean hasRxJava2() {
        if (mHasRxJava2 == null) {
            try {
                Class.forName("retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory");
                mHasRxJava2 = true;
            } catch (ClassNotFoundException ignored) {
                mHasRxJava2 = false;
            }
        }

        return mHasRxJava2;
    }

}
