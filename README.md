# Lib SimpleHttp

## 1 Explanation

An android library makes http requesting easy. 

> Note: It is not for massive uploading or downloading.

## 2 Usage

```kotlin
interface ServerAPI {

    @GET("article/list/0/json")
    suspend fun getList(): HttpResult<WanList>

}

fun example(){
    // style 1:
    lifecycleScope.launch(Dispatchers.IO) {
        apiCallNullable { serverApi.getListNullable() } onSuccess {
            Timber.d("data: $it")
        } onError {
            Timber.d(errorHandler.convert(it).toString())
        }
    }
    
    // style 2:
    val result = try {
        executeApiCallNullable { serverApi.getListNullable() }
    } catch (e: Exception) {
        ensureActive()
        Timber.d(errorHandler.convert(e).toString())
        null
    }
}
```

check out examples in the app module for more details.

## 3 Installation

```groovy
implementation "com.github.Ztiany:android-simple-http:1.3.4"
```
