package com.dicoding.asclepius.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.NewsResponse
import com.dicoding.asclepius.database.HistoryEntity
import com.dicoding.asclepius.repository.CancerRepository
import kotlinx.coroutines.launch

class CancerViewModel(application: Application) : AndroidViewModel(application) {
    private var _curImgUri = MutableLiveData<Uri?>()
    val curImgUri : MutableLiveData<Uri?> = _curImgUri
    private val repository = CancerRepository(application.applicationContext)
    private val _newsData = MutableLiveData<NewsResponse>()
    val newsData: LiveData<NewsResponse> = _newsData
    val predictHistory: LiveData<List<HistoryEntity>> = repository.getPredictHistory()

    fun fetchCancerNews(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCancerNews(apiKey)
                _newsData.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setCurrentImage(uri: Uri?) {
        _curImgUri.value = uri
    }

    fun savePrediction(history: HistoryEntity) {
        viewModelScope.launch {
            repository.insertPredictHistory(history)
        }
    }

    fun deletePrediction(history: HistoryEntity) {
        viewModelScope.launch {
            repository.deletePredictHistory(history)
        }
    }
}
