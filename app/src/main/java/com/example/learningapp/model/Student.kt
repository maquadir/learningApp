package com.example.learningapp.model

data class Student(
    val auth_token: String,
    val parent_id: String,
    val type: String,
    val user_id: String,
    val username: String
)