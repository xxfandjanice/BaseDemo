package com.crazy.demo.net

import com.crazy.demo.entity.response.VersionBean
import com.crazy.kotlin_mvvm.http.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    /**
     * APP版本升级检测
     */
    @GET("api/check-version")
    suspend fun checkVersion(@Query("platform") platform: String = "android", @Query("version") version: Long): BaseResponse<VersionBean>?

}