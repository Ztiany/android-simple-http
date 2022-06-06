package com.android.sdk.net.core.provider;

import com.android.sdk.net.core.exception.ApiErrorException;

/**
 * @author Ztiany
 */
public interface ErrorMessage {

    /**
     * 网络错误的提示消息。
     */
    CharSequence netErrorMessage(Throwable exception);

    /**
     * 服务器返回的数据格式异常的提示消息。
     */
    CharSequence serverDataErrorMessage(Throwable exception);


    /**
     * 当服务器返回的实体数据是为 null 的提示消息。
     */
    CharSequence serverNullEntityErrorMessage(Throwable exception);

    /**
     * 服务器错误（code = 500-600）的提示消息。
     */
    CharSequence serverErrorMessage(Throwable exception);

    /**
     * 客户端请求错误（code = 400-499）的提示消息
     */
    CharSequence clientRequestErrorMessage(Throwable exception);

    /**
     * API 调用错误的提示消息
     */
    CharSequence apiErrorMessage(ApiErrorException exception);

    /**
     * 未知错误的提示消息
     */
    CharSequence unknownErrorMessage(Throwable exception);

}
