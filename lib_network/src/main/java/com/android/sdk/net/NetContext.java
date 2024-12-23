package com.android.sdk.net;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.android.sdk.net.core.config.ErrorMessageFactory;
import com.android.sdk.net.core.service.ServiceFactory;
import com.android.sdk.net.core.service.ServiceHelper;
import com.android.sdk.net.impl.ErrorMessageFactoryImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;

/**
 * @author Ztiany
 */
public class NetContext {

    @SuppressLint("StaticFieldLeak") private static volatile NetContext CONTEXT;

    private final Map<String, HostConfig> mHostConfigs = new ConcurrentHashMap<>();

    public static final String DEFAULT_CONFIG = "default_host_config";

    private final ServiceHelper mServiceHelper;

    private Context mContext;

    private CommonConfig mCommonConfig;

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
    }

    void initCommonProvider(CommonConfig commonConfig) {
        mCommonConfig = commonConfig;
    }

    @MainThread
    @NonNull
    public CommonConfigBuilder newCommonConfig(Context context) {
        mContext = context;
        CommonConfigBuilder configBuilder = new CommonConfigBuilder(this);
        configBuilder.registerComponent(ErrorMessageFactory.class, new ErrorMessageFactoryImpl());
        return configBuilder;
    }

    @MainThread
    @NonNull
    public HostConfigBuilder newHostBuilder(@NonNull String flag) {
        checkIfHasBeenInitialized();
        return new HostConfigBuilder(flag, this);
    }

    void addInto(String flag, @NonNull HostConfig hostConfig) {
        if (mHostConfigs.containsKey(flag)) {
            throw new RuntimeException("The HostConfig identified as " + flag + " has been initialized");
        }
        mHostConfigs.put(flag, hostConfig);
    }

    private void checkIfHasBeenInitialized() {
        if (mContext == null) {
            throw new IllegalStateException("You should call commonConfig() then setUp() first.");
        }
    }

    @NonNull
    public CommonConfig commonConfig() {
        if (mCommonConfig == null) {
            throw new IllegalStateException("You initialize the common config first.");
        }
        return mCommonConfig;
    }

    public boolean isConnected() {
        return commonConfig().platformInteractor().isConnected();
    }

    @NonNull
    public HostConfig hostConfig(@NonNull String flag) {
        HostConfig hostConfig = mHostConfigs.get(flag);

        if (hostConfig == null) {
            throw new RuntimeException("The HostConfig identified as " + flag + " has not been initialized");
        }

        return hostConfig;
    }

    @NonNull
    public OkHttpClient httpClient() {
        return httpClient(DEFAULT_CONFIG);
    }

    @NonNull
    public OkHttpClient httpClient(@NonNull String flag) {
        return mServiceHelper.getOkHttpClient(flag, hostConfig(flag).httpConfig());
    }

    @NonNull
    public ServiceFactory serviceFactory(@NonNull String flag) {
        return mServiceHelper.getServiceFactory(flag, hostConfig(flag).httpConfig());
    }

    @NonNull
    public Context getContext() {
        return mContext;
    }

    @NonNull
    public ErrorMessageFactory getErrorMessageFactory() {
        ErrorMessageFactory component = mCommonConfig.getComponent(ErrorMessageFactory.class);
        if (component == null) {
            throw new IllegalStateException("ErrorMessageFactory is not registered.");
        }
        return component;
    }

}