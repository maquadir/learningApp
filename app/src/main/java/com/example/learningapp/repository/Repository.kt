package com.example.learningapp.repository

import com.example.learningapp.model.BodyRequest
import com.example.learningapp.network.ApiService

class Repository(private val apiService: ApiService) {

    suspend fun parentLogin(bodyRequest: BodyRequest) =
        apiService.parentLogin(bodyRequest, bodyRequest.username, bodyRequest.password)

    suspend fun childLogin(bodyRequest: BodyRequest) =
        apiService.childLogin(bodyRequest, bodyRequest.username, bodyRequest.password)

    suspend fun parentChildLogin(student_id: String) =
        apiService.parentChildLogin(student_id)

    suspend fun childLoginToken(student_id: String, parent_token: String) =
        apiService.childLoginToken(student_id, parent_token)
}
