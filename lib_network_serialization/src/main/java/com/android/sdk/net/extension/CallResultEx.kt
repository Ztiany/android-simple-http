package com.android.sdk.net.extension

import com.android.sdk.net.coroutines.CallResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException

fun CallResult<*>.throwOnError() {
    if (this is CallResult.Error) {
        throw error
    }
}

fun CallResult<*>.printIfError() {
    if (this is CallResult.Error) {
        Timber.w(error, "error of CallResult")
    }
}

fun <T> CallResult<T>.getOr(value: T): T {
    return (this as? CallResult.Success)?.data ?: value
}

fun <T> CallResult<T>.getOrNull(): T? {
    return (this as? CallResult.Success)?.data
}

fun <T> CallResult<T>.getOrThrow(): T {
    return (this as? CallResult.Success)?.data
        ?: if (this is CallResult.Error) throw error else throw NullPointerException("CallResult is not success or has no data")
}

@OptIn(ExperimentalContracts::class)
fun <T> CallResult<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is CallResult.Success)
        returns(false) implies (this@isSuccess is CallResult.Error)
    }
    return this is CallResult.Success
}

@OptIn(ExperimentalContracts::class)
fun <T> CallResult<T>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is CallResult.Error)
        returns(false) implies (this@isError is CallResult.Success)
    }
    return this is CallResult.Error
}

@OptIn(ExperimentalContracts::class)
inline fun <T, R> CallResult<T>.map(transform: (T) -> R): CallResult<R> {
    contract {
        callsInPlace(transform, InvocationKind.EXACTLY_ONCE)
    }
    return when (this) {
        is CallResult.Success -> CallResult.Success(transform(data))
        is CallResult.Error -> CallResult.Error(error)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T, R> CallResult<T>.switchMap(transform: (T) -> CallResult<R>): CallResult<R> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is CallResult.Success -> transform(data)
        is CallResult.Error -> CallResult.Error(error)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T1, T2, R> CallResult<T1>.zip(other: CallResult<T2>, transform: (T1, T2) -> R): CallResult<R> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return if (this is CallResult.Success && other is CallResult.Success) {
        CallResult.Success(transform(data, other.data))
    } else {
        (this as? CallResult.Error) ?: (other as CallResult.Error)
    }
}

fun <T> CallResult<T>.or(other: CallResult<T>): CallResult<T> {
    return when (this) {
        is CallResult.Success -> this
        else -> other
    }
}

interface CoroutineResultScope<V> : CoroutineScope {

    suspend fun <V> CallResult<V>.bind(): V

}

private class CoroutineResultScopeImpl<V>(
    delegate: CoroutineScope,
) : CoroutineResultScope<V>, CoroutineScope by delegate {

    private val mutex = Mutex()

    private var _result: CallResult<V>? = null
    val result: CallResult<V>
        get() = _result ?: throw IllegalStateException("result is null")

    // 使用 bind 支持协程的结构化编程，这样当一个协程任务异常失败时，取消其他的协程任务。
    override suspend fun <V> CallResult<V>.bind(): V {
        return if (this is CallResult.Success) {
            getOrThrow()
        } else {
            // 只取消一次，任何一个协程任务失败，都会取消其他的协程任务。
            mutex.withLock {
                if (_result == null) {
                    _result = this as CallResult.Error
                    coroutineContext.cancel()
                }
                throw CancellationException()
            }
        }
    }
}

/**
 * This extension is inspired from [如何扩展 Result，让你的代码更简洁](https://juejin.cn/post/7379509948903014451).
 */
@OptIn(ExperimentalContracts::class)
suspend fun <V, E> CallResult<V>.coroutineMap(block: suspend CoroutineResultScope<E>.(V) -> E): CallResult<E> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (this is CallResult.Success) {
        lateinit var receiver: CoroutineResultScopeImpl<E>
        return try {
            coroutineScope {
                receiver = CoroutineResultScopeImpl(this)
                val value = getOrThrow()
                with(receiver) {
                    CallResult.Success(block(value))
                }
            }
        } catch (ex: CancellationException) {
            receiver.result
        }
    } else {
        return this as CallResult.Error
    }
}