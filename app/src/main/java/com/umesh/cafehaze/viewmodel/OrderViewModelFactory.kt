package com.umesh.cafehaze.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umesh.cafehaze.model.repository.OrderRepository

class OrderViewModelFactory(
    private val repository: OrderRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OrderViewModel(repository) as T
    }
}