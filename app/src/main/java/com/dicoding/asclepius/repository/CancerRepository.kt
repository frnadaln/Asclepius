package com.dicoding.asclepius.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.NewsResponse
import com.dicoding.asclepius.database.HistoryDatabase
import com.dicoding.asclepius.database.HistoryEntity
import com.dicoding.asclepius.retrofit.ApiConfig

class CancerRepository(context: Context) {

    private val apiService = ApiConfig.getApiService()
    private val historyDao = HistoryDatabase.getInstance(context).historyDao()

    suspend fun getCancerNews(apiKey: String): NewsResponse {
        return apiService.getCancerNews(apiKey = apiKey)
    }

    fun getPredictHistory(): LiveData<List<HistoryEntity>> {
        return historyDao.getPredictHistory()
    }

    suspend fun insertPredictHistory(history: HistoryEntity) {
        historyDao.insertPredictHistory(listOf(history))
    }

    suspend fun deletePredictHistory(history: HistoryEntity) {
        historyDao.deletePredictHistory(history)
    }
}
