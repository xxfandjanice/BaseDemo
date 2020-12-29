package com.crazy.kotlin_mvvm.http

import com.crazy.kotlin_mvvm.BaseApplication
import com.crazy.kotlin_mvvm.BuildConfig
import com.crazy.kotlin_mvvm.utils.LocaleManager
import com.crazy.kotlin_mvvm.utils.Preference
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by wtc on 2019/11/1
 */
abstract class BaseRetrofit {

    private val token by Preference(Preference.TOKEN, "")

    abstract fun getBaseUrl(): String

    val mRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

     val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.addHeader("Authorization", "Bearer $token")
                return@Interceptor chain.proceed(builder.build())
            })
            .addInterceptor(LoggingInterceptor())
//                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
}