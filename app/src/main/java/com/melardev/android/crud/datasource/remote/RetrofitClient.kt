package com.melardev.android.crud.datasource.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.melardev.android.crud.CrudApplication
import com.melardev.android.crud.datasource.remote.api.RxTodoApi
import com.melardev.android.crud.datasource.remote.interceptors.HttpRequestsInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object RetrofitClient {

    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }


    private fun cache(): Cache {
        val cacheSize = (10 * 1024 * 1024).toLong()
        val httpCacheDirectory = File(CrudApplication.instance.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize)
    }


    internal fun okHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.cache(cache())
        httpClient.addInterceptor(logging)
        httpClient.addNetworkInterceptor(HttpRequestsInterceptor())
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        return httpClient.build()
    }

    @JvmStatic
    fun client(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://169.254.166.232:8080/api/") // base url must end with /
            .client(okHttpClient())
            .build()
    }

    internal fun retrofitTodoApi(): RxTodoApi {
        return client().create(RxTodoApi::class.java)
    }
}
