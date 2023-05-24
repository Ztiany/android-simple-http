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

}