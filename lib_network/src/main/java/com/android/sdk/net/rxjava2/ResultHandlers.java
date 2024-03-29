package com.android.sdk.net.rxjava2;

import com.android.sdk.net.NetContext;
import com.android.sdk.net.core.result.Result;
import com.github.dmstocking.optional.java.util.Optional;


/**
 * 用于处理 Retrofit + RxJava2 网络请求返回的结果。
 */
@SuppressWarnings("unused")
public class ResultHandlers {

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
     * 返回一个Transformer，用于统一处理网络请求返回的数据。
     */
    @SuppressWarnings("unchecked")
    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Upstream, T> _resultExtractor() {
        return (HttpResultTransformer<Upstream, Upstream, T>) DATA_TRANSFORMER;
    }

    public static <Upstream> HttpResultTransformer<Upstream, Upstream, Result<Upstream>> resultExtractor() {
        return _resultExtractor();
    }

    /**
     * 与{@link #resultExtractor()}的行为类似，但是最后把 HttpResult&lt;T&gt; 中的数据 T 用 {@link Optional} 包装后再转发到下游。
     * 适用于 HttpResult.getData() 可能为 null 的情况
     */
    @SuppressWarnings("unchecked")
    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Optional<Upstream>, T> _optionalExtractor() {
        return (HttpResultTransformer<Upstream, Optional<Upstream>, T>) OPTIONAL_TRANSFORMER;
    }

    public static <Upstream> HttpResultTransformer<Upstream, Optional<Upstream>, Result<Upstream>> optionalExtractor() {
        return _optionalExtractor();
    }

    /**
     * 不提取 HttpResult&lt;T&gt; 中的数据 T，只进行网络异常、空数据异常、错误 JSON 格式异常处理。
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