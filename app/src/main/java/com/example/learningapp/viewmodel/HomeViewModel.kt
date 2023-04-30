package com.example.learningapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.*
import com.example.learningapp.model.Student
import com.example.learningapp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _state: MutableLiveData<Student> = MutableLiveData<Student>()
    val state: MutableLiveData<Student> get() = _state

    private val errorMutableLiveData: MutableLiveData<String> = MutableLiveData()
    val errorLiveData: LiveData<String>
        get() = errorMutableLiveData


    fun parentChildLogin(student_id:String){
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val repos =
                    withContext(Dispatchers.IO) { repository.parentChildLogin(student_id) }
                updateState(repos.body())
            } catch (e: Exception) {
                updateState(null)
                updateErrorState(e)
            }
        }
    }

    private fun updateErrorState(e: Exception) {
        errorMutableLiveData.value = e.message.toString()
    }

    private fun updateState(user: Student?) {
        _state.value = user
    }
}