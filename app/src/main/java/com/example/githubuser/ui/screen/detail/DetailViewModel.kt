package com.example.githubuser.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.GitHubRepository
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: GitHubRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<DetailUserResponse>>(UiState.Loading)
    val uiState: StateFlow<UiState<DetailUserResponse>> = _uiState

    private val _followers = MutableStateFlow<UiState<List<ItemsItem>>>(UiState.Loading)
    val followers: StateFlow<UiState<List<ItemsItem>>> = _followers

    private val _following = MutableStateFlow<UiState<List<ItemsItem>>>(UiState.Loading)
    val following: StateFlow<UiState<List<ItemsItem>>> = _following

    private val _starUsers = MutableStateFlow<List<ItemsItem>>(emptyList())
    val starUsers: StateFlow<List<ItemsItem>> = _starUsers

    fun getUserDetail(userName: String) {
        viewModelScope.launch {
            repository.getUserDetail(userName)
                .catch { e -> _uiState.value = UiState.Error(e.message ?: "Gagal mengambil data") }
                .collect { result ->
                    _uiState.value = result
                }
        }
    }

    fun getUserFollowers(userName: String) {
        viewModelScope.launch {
            repository.getUserFollowers(userName)
                .catch { e ->
                    _followers.value = UiState.Error(e.message ?: "Gagal mengambil followers")
                }
                .collect { result ->
                    _followers.value = result
                }
        }
    }

    fun getUserFollowing(userName: String) {
        viewModelScope.launch {
            repository.getUserFollowing(userName)
                .catch { e ->
                    _following.value = UiState.Error(e.message ?: "Gagal mengambil following")
                }
                .collect { result ->
                    _following.value = result
                }
        }
    }
}