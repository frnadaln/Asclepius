package com.dicoding.asclepius.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CancerViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CancerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CancerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
