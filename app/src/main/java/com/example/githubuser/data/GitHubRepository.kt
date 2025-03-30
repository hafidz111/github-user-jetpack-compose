package com.example.githubuser.data

import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.data.local.room.UserDao
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.data.remote.retrofit.ApiService
import com.example.githubuser.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.await

class GitHubRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    fun getUsers(query: String): Flow<UiState<List<ItemsItem>>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiService.getUsers(query).await()
            val items = response.items

            if (items.isEmpty()) {
                emit(UiState.Error("Tidak ada data pengguna"))
            } else {
                val usersEntity = items.map { user ->
                    val localUser = userDao.getUserById(user.id)
                    UserEntity(
                        id = user.id,
                        login = user.login,
                        avatarUrl = user.avatarUrl,
                        isStar = localUser?.isStar ?: false
                    )
                }
                userDao.insertUsers(usersEntity)
                emit(UiState.Success(items))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Terjadi kesalahan"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateUserStar(userId: Int, isStar: Boolean) {
        withContext(Dispatchers.IO) {
            userDao.getAllUsers().firstOrNull()?.find { it.id == userId }?.let { user ->
                userDao.updateUser(user.copy(isStar = isStar))
            }
        }
    }

    suspend fun isUserStarred(userId: Int): Boolean {
        return userDao.isUserStarred(userId)
    }

    fun getStarUsers(): Flow<List<ItemsItem>> {
        return userDao.getStarUsers().map { entities ->
            entities.map { entity ->
                ItemsItem(
                    id = entity.id,
                    login = entity.login,
                    avatarUrl = entity.avatarUrl,
                    followersUrl = "",
                    followingUrl = ""
                )
            }
        }
    }

    fun getUserDetail(userName: String): Flow<UiState<DetailUserResponse>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiService.getDetailUser(userName).await()
            emit(UiState.Success(response))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Terjadi kesalahan saat mengambil detail pengguna"))
        }
    }

    fun getUserFollowers(userName: String): Flow<UiState<List<ItemsItem>>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiService.getUserFollowers(userName).await()
            if (response.isEmpty()) {
                emit(UiState.Error("Tidak ada followers"))
            } else {
                emit(UiState.Success(response))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Unknown error"))
        }
    }

    fun getUserFollowing(username: String): Flow<UiState<List<ItemsItem>>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiService.getUserFollowing(username).await()
            if (response.isEmpty()) {
                emit(UiState.Error("Tidak ada following"))
            } else {
                emit(UiState.Success(response))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Unknown error"))
        }
    }

    companion object {
        @Volatile
        private var instance: GitHubRepository? = null
        fun getInstance(apiService: ApiService, userDao: UserDao): GitHubRepository =
            instance ?: synchronized(this) {
                instance ?: GitHubRepository(apiService, userDao)
            }.also {
                instance = it
            }
    }
}