package com.android.sdk.net.rxjava2;

import com.android.sdk.net.NetContext;
import com.android.sdk.net.core.result.Result;

import java.util.Optional;


/**
 * It is used to process the results returned by Retrofit + RxJava2 network requests.
 */
class ResultHandlers {

    private static class ResultTransformer<Upstream, T extends Result<Upstream>> extends HttpResultTransformer<Upstream, Upstream, T> {
        ResultTransformer() {
            super(true, Result::getData, NetContext.DEFAULT_CONFIG);
        }

        ResultTransformer(String hostFlag) {
            super(true, Result::getData, hostFlag);
        }
    }

    private static class OptionalResultTransformer<Upstream, T extends Result<Upstream>> extends HttpResultTransformer<Upstream, Optional<Upstream>, T> {
        OptionalResultTransformer() {
            super(false, rResult -> Optional.ofNullable(rResult.getData()), NetContext.DEFAULT_CONFIG);
        }

        OptionalResultTransformer(String hostFlag) {
            super(false, rResult -> Optional.ofNullable(rResult.getData()), hostFlag);
        }
    }

    private static class ResultChecker<Upstream, T extends Result<Upstream>> extends HttpResultTransformer<Upstream, T, T> {
        @SuppressWarnings("unchecked")
        ResultChecker() {
            super(false, rResult -> (T) rResult, NetContext.DEFAULT_CONFIG);
        }

        @SuppressWarnings("unchecked")
        ResultChecker(String hostFlag) {
            super(false, rResult -> (T) rResult, hostFlag);
        }
    }

    private static final ResultTransformer DATA_TRANSFORMER = new ResultTransformer();

    private static final OptionalResultTransformer OPTIONAL_TRANSFORMER = new OptionalResultTransformer();

    private static final ResultChecker RESULT_CHECKER = new ResultChecker();

    /**
     * A transformer that processes the data returned by the network request.
     */
    @SuppressWarnings("unchecked")
    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Upstream, T> _resultExtractor() {
        return (HttpResultTransformer<Upstream, Upstream, T>) DATA_TRANSFORMER;
    }

    public static <Upstream> HttpResultTransformer<Upstream, Upstream, Result<Upstream>> resultExtractor() {
        return _resultExtractor();
    }

    /**
     * The behavior is similar to {@link #resultExtractor()}, but the data T in HttpResult&lt;T&gt; is wrapped in {@link Optional} before being forwarded to the downstream.
     */
    @SuppressWarnings("unchecked")
    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Optional<Upstream>, T> _optionalExtractor() {
        return (HttpResultTransformer<Upstream, Optional<Upstream>, T>) OPTIONAL_TRANSFORMER;
    }

    public static <Upstream> HttpResultTransformer<Upstream, Optional<Upstream>, Result<Upstream>> optionalExtractor() {
        return _optionalExtractor();
    }

    /**
     * Does not extract the data T in HttpResult&lt;T&gt;, only handles network exceptions, empty data exceptions, and error JSON format exceptions.
     */
    @SuppressWarnings("unchecked")
    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, T, T> _resultChecker() {
        return (HttpResultTransformer<Upstream, T, T>) RESULT_CHECKER;
    }

    public static <Upstream> HttpResultTransformer<Upstream, Result<Upstream>, Result<Upstream>> resultChecker() {
        return _resultChecker();
    }

    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Upstream, T> _newExtractor(String hostFlag) {
        return new ResultTransformer<>(hostFlag);
    }

    public static <Upstream> HttpResultTransformer<Upstream, Upstream, Result<Upstream>> newExtractor(String hostFlag) {
        return _newExtractor(hostFlag);
    }

    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Optional<Upstream>, T> _newOptionalExtractor(String hostFlag) {
        return new OptionalResultTransformer<>(hostFlag);
    }

    public static <Upstream> HttpResultTransformer<Upstream, Optional<Upstream>, Result<Upstream>> newOptionalExtractor(String hostFlag) {
        return _newOptionalExtractor(hostFlag);
    }

    public static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, T, T> newResultChecker(String hostFlag) {
        return new ResultChecker<>(hostFlag);
    }

}