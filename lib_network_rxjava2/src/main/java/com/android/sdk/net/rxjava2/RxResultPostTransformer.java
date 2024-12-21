package com.android.sdk.net.rxjava2;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;

/**
 * Post processing after the network request result is processed by {@link ResultHandlers},
 * you can add this interface to add unified reprocessing logic, such as retry after token implementation.
 *
 * @author Ztiany
 */
public interface RxResultPostTransformer<Data> extends
        ObservableTransformer<Data, Data>, FlowableTransformer<Data, Data>, SingleTransformer<Data, Data> {

}