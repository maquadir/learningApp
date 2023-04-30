package com.example.learningapp.network

import com.example.learningapp.model.BodyRequest
import com.example.learningapp.model.Parent
import com.example.learningapp.model.Student
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("/login")
    suspend fun parentLogin(
        @Body body: BodyRequest,
        @Query("parent_username") username: String,
        @Query("parent_password") password: String
    ): Response<Parent>

    @POST("/login")
    suspend fun childLogin(
        @Body body: BodyRequest,
        @Query("child_username") username: String,
        @Query("child_password") password: String
    ): Response<Student>

    @POST("/login/student/")
    suspend fun parentChildLogin(@Query("student_id") student_id: String): Response<Student>

    @POST("/loginchildtoken/")
    suspend fun childLoginToken(
        @Query("student_id") student_id: String,
        @Query("auth_token") auth_token: String
    ): Response<Student>

    companion object {
        val instance: ApiService by lazy {
            val authInterceptor = AuthInterceptor()

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://18e2d77a-1039-4ecf-8f70-14bcbabf3e47.mock.pstmn.io")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            retrofit.create(ApiService::class.java)
        }
    }

}
