package com.example.learningapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.*
import com.example.learningapp.model.BodyRequest
import com.example.learningapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

enum class User {
    PARENT, STUDENT
}

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _state: MutableLiveData<Any> = MutableLiveData<Any>()
    val state: MutableLiveData<Any> get() = _state

    private val errorMutableLiveData: MutableLiveData<String> = MutableLiveData()
    val errorLiveData: LiveData<String>
        get() = errorMutableLiveData

    fun login(isStudent: Boolean, body: BodyRequest) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val repos =
                    withContext(Dispatchers.IO) {
                        if (!isStudent) repository.parentLogin(body)
                        else repository.childLogin(body)
                    }
                updateState(repos.body())
            } catch (e: Exception) {
                updateErrorState(e)
            }
        }
    }

    private fun updateErrorState(e: Exception) {
        errorMutableLiveData.value = e.message.toString()
    }

    private fun updateState(user: Any?) {
        _state.value = user
    }
}