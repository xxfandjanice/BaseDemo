package com.crazy.demo.net

import com.crazy.demo.BuildConfig
import com.crazy.kotlin_mvvm.http.BaseRetrofit

object RetrofitClient:BaseRetrofit() {

    override fun getBaseUrl() = BuildConfig.BASE_URL

    val apiService:ApiService by lazy { mRetrofit.create(ApiService::class.java) }
}