package com.example.githubuser.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.GitHubRepository
import com.example.githubuser.ui.screen.detail.DetailViewModel
import com.example.githubuser.ui.screen.home.HomeViewModel
import com.example.githubuser.ui.screen.star.StarViewModel

class ViewModelFactory(private val repository: GitHubRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(StarViewModel::class.java)) {
            return StarViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}