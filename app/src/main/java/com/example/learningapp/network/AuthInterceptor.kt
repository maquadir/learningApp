package com.example.learningapp.network

import com.example.learningapp.MyApp
import com.example.learningapp.getParentToken
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getFromStorage()
        val request = chain.request()
        if (!token.isNullOrEmpty()) {
            val newRequest = request
                .newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(request)
    }

    private fun getFromStorage(): String? = MyApp.context?.getParentToken()
}