package me.ztiany.simple.http.example

import io.reactivex.Single
import retrofit2.http.GET

interface ServerAPI {

    @GET("article/list/0/json")
    suspend fun getList(): HttpResult<WanList>

    @GET("article/list/0/json")
    suspend fun getListNullable(): HttpResult<WanList?>

    @GET("article/list/0/json")
    suspend fun getListAllNullable(): HttpResult<WanList?>?

    @GET("article/list/0/json")
    fun getRxList(): Single<HttpResult<WanList>>

    @GET("mock/httpError")
    suspend fun mockHttpError(): HttpResult<WanList?>?

    @GET("mock/apiError")
    suspend fun mockApiError(): HttpResult<WanList?>?

    @GET("mock/parsingError")
    suspend fun mockParsingError(): HttpResult<WanList?>?

}