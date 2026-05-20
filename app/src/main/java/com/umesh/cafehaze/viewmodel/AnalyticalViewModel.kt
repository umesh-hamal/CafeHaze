package com.umesh.cafehaze.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umesh.cafehaze.data.AnalyticsRepository
import com.umesh.cafehaze.model.data.SalesPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: AnalyticsRepository
) : ViewModel() {

    var salesData by mutableStateOf<List<SalesPoint>>(emptyList())
        private set

    init {
        loadAnalytics()
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            salesData = repository.getWeeklySales()
        }
    }
}