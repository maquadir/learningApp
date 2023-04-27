package com.example.learningapp.model

data class Parent(
    val auth_token: String,
    val student_ids: List<String>,
    val type: String,
    val user_id: String,
    val username: String
)