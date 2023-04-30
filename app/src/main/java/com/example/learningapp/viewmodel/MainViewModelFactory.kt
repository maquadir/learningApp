package com.example.learningapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learningapp.repository.Repository

//viewmodel factory for creating viewmodel with dependencies
class MainViewModelFactory(
    private val propertiesRepository: Repository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        MainViewModel::class.java -> MainViewModel(propertiesRepository)
        else -> HomeViewModel(propertiesRepository)
    } as T
}