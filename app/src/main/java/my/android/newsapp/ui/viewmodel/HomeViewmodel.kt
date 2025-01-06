package my.android.newsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import my.android.newsapp.data.model.Article
import my.android.newsapp.data.repository.NewsRepository
import javax.inject.Inject

@HiltViewModel
// ViewModel class responsible for managing UI-related data and business logic for the home screen
class HomeViewmodel @Inject constructor(
    private val repository: NewsRepository  // Injects the NewsRepository
) : ViewModel() {

    // Mutable state flow that represents the current UI state (loading, success, or error)
    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    // Publicly exposed immutable state flow for UI to observe the current state
    val uiState = _uiState.asStateFlow()

    private val _country = MutableStateFlow("us")
    val selectedCountry = _country.asStateFlow()

    private val _category = MutableStateFlow("general")
    val selectedCategory = _category.asStateFlow()

    // Initially loading the headlines based on the default country and category
    init {
        loadHeadlines()
    }

    // Function to load news headlines based on current country and category
    fun loadHeadlines() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            val result = repository.getHeadlines(_country.value, _category.value)
            _uiState.value = when {
                result.isSuccess -> NewsUiState.Success(result.getOrNull() ?: emptyList())
                result.isFailure -> NewsUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                else -> {NewsUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")}
            }
        }
    }

    // Function to update the country and reload the headline based on the new country
    fun updateCountry(country: String) {
        _country.value = country
        loadHeadlines()
    }
    // Function to update the category and reload the headline based on the new category
    fun updateCategory(category: String) {
        _category.value = category
        loadHeadlines()
    }
}

// Sealed class representing the different states of the UI
sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}