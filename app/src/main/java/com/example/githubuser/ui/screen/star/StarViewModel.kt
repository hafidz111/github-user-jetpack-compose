package com.example.githubuser.ui.screen.star

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.GitHubRepository
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StarViewModel(private val repository: GitHubRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<ItemsItem>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<ItemsItem>>> = _uiState

    private val _starUsers = MutableStateFlow<List<ItemsItem>>(emptyList())
    val starUsers: StateFlow<List<ItemsItem>> = _starUsers

    init {
        observeStarUsers()
    }

    private fun observeStarUsers() {
        viewModelScope.launch {
            repository.getStarUsers().collect { starredUsers ->
                _starUsers.value = starredUsers
                _uiState.value = UiState.Success(starredUsers)
            }
        }
    }

    fun toggleStar(user: ItemsItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val isStar = repository.isUserStarred(user.id)
            repository.updateUserStar(user.id, !isStar)

            getStarUsers()
        }
    }

    fun getStarUsers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.getStarUsers().collect { result ->
                    _uiState.value = UiState.Success(result)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }

        }
    }
}