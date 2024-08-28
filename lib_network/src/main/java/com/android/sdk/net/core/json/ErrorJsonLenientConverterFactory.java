package com.android.sdk.net.core.json;


import androidx.annotation.NonNull;

import com.android.sdk.net.core.exception.ServerErrorException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Json Deserialization Error Handling, for more details, see <a href="http://blog.piasy.com/2016/09/04/RESTful-Android-Network-Solution-2/">RESTful-Android-Network-Solution-2</a>.
 *
 * @author Ztiany
 */
public class ErrorJsonLenientConverterFactory extends Converter.Factory {

    private final Converter.Factory mGsonConverterFactory;

    public ErrorJsonLenientConverterFactory(Converter.Factory gsonConverterFactory) {
        mGsonConverterFactory = gsonConverterFactory;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            @NonNull Type type,
            @NonNull Annotation[] parameterAnnotations,
            @NonNull Annotation[] methodAnnotations,
            @NonNull Retrofit retrofit
    ) {
        return mGsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            @NonNull Type type,
            @NonNull Annotation[] annotations,
            @NonNull Retrofit retrofit
    ) {
        final Converter<ResponseBody, ?> delegateConverter = mGsonConverterFactory.responseBodyConverter(type, annotations, retrofit);
        assert delegateConverter != null;

        Timber.d("responseBodyConverter --> type is %s", type);

        return (Converter<ResponseBody, Object>) value -> {
            try {
                return delegateConverter.convert(value);
            } catch (Exception e/*JsonSyntaxException、IOException or MalformedJsonException*/) {
                /*
                 * If the conversion fails,that means the server return data is not what we expected.
                 * We just throw a ServerErrorException, and the error should be handled by the subscriber.
                 */
                Timber.e(e, "Json covert error --> error, type is %s", type);
                throw new ServerErrorException(ServerErrorException.SERVER_DATA_ERROR);
            }
        };
    }

}