package com.umesh.cafehaze.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umesh.cafehaze.model.data.BillWithItems
import com.umesh.cafehaze.model.repository.BillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillViewModel @Inject constructor(
    private val repo: BillRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    val bills: StateFlow<List<BillWithItems>> =
        repo.getAllBills()
            .onEach { _loading.value = false } // 🔥 stops loading after first emit
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}