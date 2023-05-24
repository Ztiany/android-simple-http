package com.android.sdk.net.rxjava2

import com.android.sdk.net.core.result.Result
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single


fun <T : Result<E>, E> Observable<T>.optionalExtractor(): Observable<Optional<E>> {
    return this.compose(ResultHandlers.optionalExtractor())
}

fun <T : Result<E>, E> Observable<T>.resultExtractor(): Observable<E> {
    return this.compose(ResultHandlers.resultExtractor())
}

fun <E, T : Result<E>> Observable<T>.resultChecker(): Observable<Result<E>> {
    return (this.compose(ResultHandlers.resultChecker()))
}

fun <T : Result<E>, E> Flowable<T>.optionalExtractor(): Flowable<Optional<E>> {
    return this.compose(ResultHandlers.optionalExtractor())
}

fun <T : Result<E>, E> Flowable<T>.resultExtractor(): Flowable<E> {
    return this.compose(ResultHandlers.resultExtractor())
}

fun <E, T : Result<E>> Flowable<T>.resultChecker(): Flowable<Result<E>> {
    return (this.compose(ResultHandlers.resultChecker()))
}

fun <T : Result<E>, E> Single<T>.optionalExtractor(): Single<Optional<E>> {
    return this.compose(ResultHandlers.optionalExtractor())
}

fun <T : Result<E>, E> Single<T>.resultExtractor(): Single<E> {
    return this.compose(ResultHandlers.resultExtractor())
}

fun <E, T : Result<E>> Single<T>.resultChecker(): Single<Result<E>> {
    return (this.compose(ResultHandlers.resultChecker()))
}

fun <T : Result<E>, E> Observable<T>.optionalExtractor(hostFlag: String): Observable<Optional<E>> {
    return this.compose(ResultHandlers.newOptionalExtractor(hostFlag))
}

fun <T : Result<E>, E> Observable<T>.resultExtractor(hostFlag: String): Observable<E> {
    return this.compose(ResultHandlers.newExtractor(hostFlag))
}

fun <E, T : Result<E>> Observable<T>.resultChecker(hostFlag: String): Observable<Result<E>> {
    return (this.compose(ResultHandlers.newResultChecker(hostFlag)))
}

fun <T : Result<E>, E> Flowable<T>.optionalExtractor(hostFlag: String): Flowable<Optional<E>> {
    return this.compose(ResultHandlers.newOptionalExtractor(hostFlag))
}

fun <T : Result<E>, E> Flowable<T>.resultExtractor(hostFlag: String): Flowable<E> {
    return this.compose(ResultHandlers.newExtractor(hostFlag))
}

fun <E, T : Result<E>> Flowable<T>.resultChecker(hostFlag: String): Flowable<Result<E>> {
    return (this.compose(ResultHandlers.newResultChecker(hostFlag)))
}

fun <T : Result<E>, E> Single<T>.optionalExtractor(hostFlag: String): Single<Optional<E>> {
    return this.compose(ResultHandlers.newOptionalExtractor(hostFlag))
}

fun <T : Result<E>, E> Single<T>.resultExtractor(hostFlag: String): Single<E> {
    return this.compose(ResultHandlers.newExtractor(hostFlag))
}

fun <E, T : Result<E>> Single<T>.resultChecker(hostFlag: String): Single<Result<E>> {
    return (this.compose(ResultHandlers.newResultChecker(hostFlag)))
}