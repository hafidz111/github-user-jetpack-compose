package com.example.githubuser.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Star : Screen("start")
    data object Profile : Screen("profile")
    data object Detail : Screen("detail/{userName}") {
        fun createRoute(userName: String) = "detail/$userName"
    }
}