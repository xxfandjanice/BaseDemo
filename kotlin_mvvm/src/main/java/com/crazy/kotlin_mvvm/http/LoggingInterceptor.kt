package com.crazy.kotlin_mvvm.http

import android.util.Log
import com.crazy.kotlin_mvvm.BuildConfig
import com.crazy.kotlin_mvvm.ext.formatJson
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * Created by wtc on 2019/11/1
 */
class LoggingInterceptor(var isLogEnable: Boolean = BuildConfig.DEBUG) : Interceptor {

    companion object {
        private const val TAG = "API_LOG"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        if (!isLogEnable) {
            return chain.proceed(request)
        }

        val charset = Charset.forName("UTF-8")
        val buffer = Buffer()
        val requestBody = request.body()

        val startNs = System.nanoTime()
        val response = chain.proceed(request)
        val tookMs =
            TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val requestHeaders = request.headers()

        val responseHeaders = response.headers()

        Log.d(
            TAG,
            "╔════════════════════════════════════════════════════════════════════════════════════════"
        )
        Log.d(TAG, String.format("║ 请求地址 %s %s", request.method(), request.url()))
        Log.d(
            TAG,
            "╟────────────────────────────────────────────────────────────────────────────────────────"
        )
        for (i in 0 until requestHeaders.size()) {
            val headerName = requestHeaders.name(i)
            val headerValue = requestHeaders.get(headerName)
            Log.d(TAG, String.format("║ 请求头 %s : %s", headerName, headerValue))
        }

        Log.d(
            TAG,
            "╟────────────────────────────────────────────────────────────────────────────────────────"
        )
        if (requestBody != null) {
            requestBody.writeTo(buffer)
            Log.d(TAG, String.format("║ 请求参数 %s", buffer.readString(charset)))
            Log.d(
                TAG,
                "╟────────────────────────────────────────────────────────────────────────────────────────"
            )
        }

        Log.d(
            TAG,
            String.format(
                "║ 请求结果 %s %s%s",
                response.code(),
                response.request().url(),
                "($tookMs)ms"
            )
        )
        Log.d(
            TAG,
            "╟────────────────────────────────────────────────────────────────────────────────────────"
        )

//        for (i in 0 until responseHeaders.size()) {
//            val headerName = responseHeaders.name(i)
//            val headerValue = responseHeaders.get(headerName)
//            Log.d(TAG, String.format("║ 响应头 %s : %s", headerName, headerValue))
//        }
//        Log.d(
//            TAG,
//            "╟────────────────────────────────────────────────────────────────────────────────────────"
//        )
        val responseBody = response.body()
        val source = responseBody?.source()
        source?.run {
            request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val bufferS = buffer()
            val contentType = responseBody.contentType()
            if (contentType != null) {
                val json = bufferS.clone().readString(contentType.charset(charset)!!)
                Log.d(TAG, "║ 返回数据")
                val con = json.formatJson().split("\n")
                for (line in con) {
                    Log.d(TAG, "║$line")
                }
            }
        }

        Log.d(
            TAG,
            "╚════════════════════════════════════════════════════════════════════════════════════════"
        )
        return response
    }

}