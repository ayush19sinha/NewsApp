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
class HomeViewmodel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _country = MutableStateFlow("us")
    val selectedCountry = _country.asStateFlow()

    private val _category = MutableStateFlow("general")
    val selectedCategory = _category.asStateFlow()

    init {
        loadHeadlines()
    }

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

    fun updateCountry(country: String) {
        _country.value = country
        loadHeadlines()
    }

    fun updateCategory(category: String) {
        _category.value = category
        loadHeadlines()
    }
}

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}