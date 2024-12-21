package com.android.sdk.net

import com.android.sdk.net.core.result.Result
import com.android.sdk.net.optional.Optional
import com.android.sdk.net.rxjava2.internalOptionalExtractor
import com.android.sdk.net.rxjava2.internalResultChecker
import com.android.sdk.net.rxjava2.internalResultExtractor
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

context(ServiceContext<*>)
  fun <T : Result<E>, E> Observable<T>.optionalExtractor(): Observable<Optional<E>> {
    return this.internalOptionalExtractor(hostFlag)
}

context(ServiceContext<*>)
fun <T : Result<E>, E> Observable<T>.resultExtractor(): Observable<E> {
    return this.internalResultExtractor(hostFlag)
}

context(ServiceContext<*>)
fun <E, T : Result<E>> Observable<T>.resultChecker(): Observable<Result<E>> {
    return this.internalResultChecker(hostFlag)
}

context(ServiceContext<*>)
fun <T : Result<E>, E> Flowable<T>.optionalExtractor(): Flowable<Optional<E>> {
    return this.internalOptionalExtractor(hostFlag)
}

context(ServiceContext<*>)
fun <T : Result<E>, E> Flowable<T>.resultExtractor(): Flowable<E> {
    return this.internalResultExtractor(hostFlag)
}

context(ServiceContext<*>)
fun <E, T : Result<E>> Flowable<T>.resultChecker(): Flowable<Result<E>> {
    return this.internalResultChecker(hostFlag)
}

context(ServiceContext<*>)
fun <T : Result<E>, E> Single<T>.optionalExtractor(): Single<Optional<E>> {
    return this.internalOptionalExtractor(hostFlag)
}

context(ServiceContext<*>)
fun <T : Result<E>, E> Single<T>.resultExtractor(): Single<E> {
    return this.internalResultExtractor(hostFlag)
}

context(ServiceContext<*>)
fun <E, T : Result<E>> Single<T>.resultChecker(): Single<Result<E>> {
    return this.internalResultChecker(hostFlag)
}