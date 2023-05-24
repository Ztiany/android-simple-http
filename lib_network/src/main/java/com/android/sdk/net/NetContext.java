package com.android.sdk.net;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.android.sdk.net.core.exception.ErrorMessageFactory;
import com.android.sdk.net.core.service.ServiceFactory;
import com.android.sdk.net.core.service.ServiceHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;

/**
 * @author Ztiany
 */
public class NetContext {

    @SuppressLint("StaticFieldLeak") private static volatile NetContext CONTEXT;

    private final Map<String, HostConfigProvider> mProviderMap = new ConcurrentHashMap<>();

    public static final String DEFAULT_CONFIG = "default_host_config";

    private final ServiceHelper mServiceHelper;

    private Context mContext;

    private CommonProvider mCommonProvider;

    private final ErrorMessageFactory mErrorMessageFactory;

    public static NetContext get() {
        if (CONTEXT == null) {
            synchronized (NetContext.class) {
                if (CONTEXT == null) {
                    CONTEXT = new NetContext();
                }
            }
        }
        return CONTEXT;
    }

    private NetContext() {
        mServiceHelper = new ServiceHelper();
        mErrorMessageFactory = new ErrorMessageFactoryImpl();
    }

    void initCommonProvider(CommonProvider commonProvider) {
        mCommonProvider = commonProvider;
    }

    @MainThread
    public CommonBuilder newCommonConfig(Context context) {
        mContext = context;
        return new CommonBuilder(this);
    }

    @MainThread
    public HostConfigBuilder newHostBuilder(@NonNull String flag) {
        checkIfHasBeenInitialized();
        return new HostConfigBuilder(flag, this);
    }

    void addInto(String flag, @NonNull HostConfigProvider hostConfigProvider) {
        if (mProviderMap.containsKey(flag)) {
            throw new RuntimeException("The HostConfigProvider identified as " + flag + " has been initialized");
        }
        mProviderMap.put(flag, hostConfigProvider);
    }

    private void checkIfHasBeenInitialized() {
        if (mContext == null) {
            throw new IllegalStateException("You should call commonConfig() then setUp() first.");
        }
    }

    public CommonProvider commonProvider() {
        return mCommonProvider;
    }

    public boolean isConnected() {
        return commonProvider().platformInteractor().isConnected();
    }

    @NonNull
    public HostConfigProvider hostConfigProvider(@NonNull String flag) {
        HostConfigProvider hostConfigProvider = mProviderMap.get(flag);

        if (hostConfigProvider == null) {
            throw new RuntimeException("The HostNetProvider identified as " + flag + " has not been initialized");
        }

        return hostConfigProvider;
    }

    public OkHttpClient httpClient() {
        return httpClient(DEFAULT_CONFIG);
    }

    public OkHttpClient httpClient(@NonNull String flag) {
        return mServiceHelper.getOkHttpClient(flag, hostConfigProvider(flag).httpConfig());
    }

    public ServiceFactory serviceFactory(@NonNull String flag) {
        return mServiceHelper.getServiceFactory(flag, hostConfigProvider(flag).httpConfig());
    }

    public Context getContext() {
        return mContext;
    }

    public ErrorMessageFactory getErrorMessageFactory() {
        return mErrorMessageFactory;
    }

}