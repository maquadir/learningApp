package com.example.learningapp

import android.content.Context
import com.example.learningapp.model.AuthData
import java.io.IOException

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

fun Context.getParentToken(): String {
    val sharedPreference = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
    return sharedPreference?.getString("parent_token", "") ?: ""
}

fun Context.getLoggedInChildData(): AuthData {
    val sharedPreference = getSharedPreferences("LOGGED_IN_CHILD", Context.MODE_PRIVATE)
    val authToken = sharedPreference?.getString("auth_token", "") ?: ""
    val studentId = sharedPreference?.getString("student_id", "") ?: ""
    return AuthData(authToken, studentId)
}

fun Context.setChildLoggedIn(user_id: String, auth_token: String) {
    val sharedPreference = getSharedPreferences("LOGGED_IN_CHILD", Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()
    editor.putString("student_id", user_id ?: "")
    editor.putString("auth_token", auth_token ?: "")
    editor.apply()
}

fun Context.clearPreferences(preference_name: String) {
    val sharedPreference = getSharedPreferences(preference_name, Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()
    editor.clear()
    editor.apply()
}

fun Context.clearTokenPreferences(preference_name: String, key: String) {
    val sharedPreference = getSharedPreferences(preference_name, Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()
    editor.remove(key)
    editor.apply()
}

fun Context.setToken(key: String, token: String?) {
    val sharedPreference = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
    val editor = sharedPreference.edit()
    editor.putString(key, token ?: "")
    editor.apply()
}

