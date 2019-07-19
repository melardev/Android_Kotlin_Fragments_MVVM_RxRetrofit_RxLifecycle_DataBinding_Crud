package com.melardev.android.crud.datasource.remote.api

import com.melardev.android.crud.datasource.remote.dtos.responses.SuccessResponse
import com.melardev.android.crud.datasource.common.entities.Todo
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface RxTodoApi {
    @GET("todos")
    fun fetchAll(): Observable<List<Todo>>

    @GET("todos/{id}")
    fun fetchById(@Path("id") id: Long): Observable<Todo>

    @POST("todos")
    fun create(@Body todo: Todo): Observable<Todo>

    @PUT("todos/{id}")
    fun update(@Path("id") id: Long, @Body todo: Todo): Observable<Todo>

    @DELETE("todos/{id}")
    fun delete(@Path("id") id: Long): Observable<Response<SuccessResponse?>>
}
