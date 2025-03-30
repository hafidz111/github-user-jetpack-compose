package com.example.githubuser.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.GitHubRepository
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: GitHubRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<ItemsItem>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<ItemsItem>>> = _uiState

    private val _starUsers = MutableStateFlow<List<ItemsItem>>(emptyList())
    val starUsers: StateFlow<List<ItemsItem>> = _starUsers

    init {
        searchUsers("abdul")
        getStarUsers()
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            repository.getUsers(query).collect { result ->
                _uiState.value = result
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

    private fun getStarUsers() {
        viewModelScope.launch {
            repository.getStarUsers().collectLatest { result ->
                _starUsers.value = result
            }
        }
    }
}