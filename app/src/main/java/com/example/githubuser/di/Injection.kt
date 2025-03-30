package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.GitHubRepository
import com.example.githubuser.data.local.room.UserDatabase
import com.example.githubuser.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): GitHubRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val userDao = database.userDao()
        return GitHubRepository.getInstance(apiService, userDao)
    }
}