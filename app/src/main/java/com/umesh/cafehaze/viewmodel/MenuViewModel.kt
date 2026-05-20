package com.umesh.cafehaze.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umesh.cafehaze.model.data.Category
import com.umesh.cafehaze.model.data.MenuItem
import com.umesh.cafehaze.model.repository.MenuRepository
import com.umesh.cafehaze.model.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: MenuRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val menuItems: StateFlow<List<MenuItem>> = _menuItems

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadMenu()
    }

    fun loadMenu() {
        if (_menuItems.value.isNotEmpty() && _categories.value.isNotEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val items = repository.getMenuItems()
                val cats = repository.getCategories()

                val favIds = try {
                    favoriteRepository.getFavorites().map { it.itemId }.toSet()
                } catch (e: Exception) {
                    emptySet()
                }

                _menuItems.value = items.map {
                    it.copy(isFavorite = it.id in favIds)
                }

                _categories.value = cats

                Log.d("MENU_VM", "Loaded ${_menuItems.value.size} items")

            } catch (e: Exception) {
                _errorMessage.value = "Failed to load menu"
                Log.e("MENU_VM_ERROR", e.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(item: MenuItem) {
        viewModelScope.launch {
            try {
                val currentList = _menuItems.value
                val isFav = currentList.find { it.id == item.id }?.isFavorite ?: false

                if (isFav) {
                    favoriteRepository.removeFavorite(item.id)
                } else {
                    favoriteRepository.addFavorite(item.id)
                }

                _menuItems.value = currentList.map {
                    if (it.id == item.id) it.copy(isFavorite = !isFav)
                    else it
                }

            } catch (e: Exception) {
                Log.e("FAV_ERROR", e.message.toString())
            }
        }
    }

    fun selectCategory(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
        _searchQuery.value = ""
    }

    fun updateSearch(query: String) {
        _searchQuery.value = query

        if (query.isNotBlank()) {
            _selectedCategoryId.value = null
        }
    }

    val filteredItems: StateFlow<List<MenuItem>> =
        combine(_menuItems, _selectedCategoryId, _searchQuery) { items, categoryId, query ->

            val q = query.trim().lowercase()

            items.filter { item ->
                if (q.isNotEmpty()) {
                    item.name.lowercase().contains(q)
                } else {
                    categoryId == null || item.categoryId == categoryId
                }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val favoriteItems: StateFlow<List<MenuItem>> =
        _menuItems.map { list ->
            list.filter { it.isFavorite }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}