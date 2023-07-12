package me.ztiany.simple.http.example

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.sdk.net.NetContext
import com.android.sdk.net.coroutines.onError
import com.android.sdk.net.coroutines.onSuccess
import com.android.sdk.net.extension.createServiceContext
import com.android.sdk.net.extension.defaultServiceFactory
import com.android.sdk.net.extension.onError
import com.android.sdk.net.extension.tryCall
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

private fun Throwable.rethrowIfCancellation() {
    if (this is CancellationException) {
        throw this
    }
}

class MainActivity : AppCompatActivity() {

    private var serverContext = NetContext.get().defaultServiceFactory().createServiceContext<ServerAPI>()

    private var serverContextMocked = NetContext.get().serviceFactory("Mock").createServiceContext<ServerAPI>()

    private val errorHandler = NetContext.get().errorMessageFactory

    private var executingJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btn_execute_request).setOnClickListener {
            executingJob = executeRequest()
            lifecycleScope.launch {
                delay(200)
                executingJob?.cancel()
            }
        }

        findViewById<View>(R.id.btn_execute_request_nullable).setOnClickListener {
            executingJob = executeRequestNullable()
            lifecycleScope.launch {
                delay(200)
                executingJob?.cancel()
            }
        }

        findViewById<View>(R.id.btn_get_result).setOnClickListener {
            executingJob = getResult()
            lifecycleScope.launch {
                delay(200)
                executingJob?.cancel()
            }
        }

        findViewById<View>(R.id.btn_get_result_nullable).setOnClickListener {
            getResultNullable()
        }

        findViewById<View>(R.id.btn_test_rx).setOnClickListener {
            rxRequest()
        }

        findViewById<View>(R.id.btn_test_mock).setOnClickListener {
            requestMock()
        }
    }

    private fun executeRequest() = lifecycleScope.launch(Dispatchers.IO) {
        try {
            val data = serverContext.executeApiCall { getList() }
            Timber.d("data: $data")
        } catch (e: Throwable) {
            e.rethrowIfCancellation()
            Timber.d("onError: $e")
        }
        Timber.d("I am still alive.")
    }

    private fun executeRequestNullable() = lifecycleScope.launch(Dispatchers.IO) {
        val data = try {
            serverContext.executeApiCallNullable { getListNullable() }
        } catch (e: Exception) {
            e.rethrowIfCancellation()
            Timber.d(errorHandler.createMessage(e).toString())
            null
        }
        Timber.d("data: $data")
    }

    private fun getResult() = lifecycleScope.launch(Dispatchers.IO) {
        serverContext.apiCall { getList() } onSuccess {
            Timber.d("data: $it")
        } onError {
            Timber.d(errorHandler.createMessage(it).toString())
        }
        Timber.d("I am still alive.")
    }

    private fun getResultNullable() = lifecycleScope.launch(Dispatchers.IO) {
        val callResult = serverContext.apiCallNullable { getListNullable() }
        callResult onSuccess {
            Timber.d("data: $it")
        } onError {
            Timber.d(errorHandler.createMessage(it).toString())
        }
        Timber.d("I am still alive.")
    }

    private fun requestMock() = lifecycleScope.launch(Dispatchers.IO) {
        serverContextMocked.apiCallNullable { getListAllNullable() } onSuccess {
            Timber.d("apiCallNullable data: $it")
        } onError {
            Timber.e(it, "apiCallNullable")
            Timber.d("apiCallNullable ${errorHandler.createMessage(it)}")
        }
    }

    private fun rxRequest() = with(serverContext) {
        service.getRxList()
            .subscribeOn(Schedulers.io())
            .resultExtractor()
            .subscribe(
                {
                    Timber.d("rxRequest data: $it")
                },
                {
                    Timber.e(it, "rxRequest")
                }
            )
    }

}