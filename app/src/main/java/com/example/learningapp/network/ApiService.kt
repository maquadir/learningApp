package com.example.learningapp.network

import com.example.learningapp.model.Parent
import com.example.learningapp.model.Student
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("/login")
    suspend fun parentLogin(): Parent

    @POST("/login")
    suspend fun childLogin(): Student

    @POST("/login/student/{student-user-id}")
    suspend fun parentChildLogin(): Student

    companion object {
        val instance: ApiService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.xyz.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(ApiService::class.java)
        }
    }

}
