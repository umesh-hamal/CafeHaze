package com.umesh.cafehaze.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umesh.cafehaze.model.data.Category
import com.umesh.cafehaze.model.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: MenuRepository
) : ViewModel() {

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    var selectedCategoryId by mutableStateOf<Int?>(null)
        private set

    init {
        viewModelScope.launch {
            categories = repository.getCategories()
        }
    }

    fun selectCategory(categoryId: Int?) {
        selectedCategoryId = categoryId
    }
}