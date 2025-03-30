package com.example.githubuser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.githubuser.ui.component.BottomBar
import com.example.githubuser.ui.component.TopBar
import com.example.githubuser.ui.navigation.Screen
import com.example.githubuser.ui.screen.detail.DetailScreen
import com.example.githubuser.ui.screen.home.HomeScreen
import com.example.githubuser.ui.screen.profile.ProfileScreen
import com.example.githubuser.ui.screen.star.StarScreen

@Composable
fun GitHubUserApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val userName =
        if (currentRoute?.startsWith(Screen.Detail.route.substringBefore("/{userName}")) == true) {
            navBackStackEntry?.arguments?.getString("userName") ?: ""
        } else ""

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                when (currentRoute) {
                    Screen.Home.route -> {
                        TopBar(
                            title = stringResource(R.string.app_name),
                            showBackButton = false,
                            showProfileButton = true,
                            onProfileClick = { navController.navigate(Screen.Profile.route) }
                        )
                    }

                    Screen.Star.route -> {
                        TopBar(
                            title = stringResource(R.string.starred_users),
                            navController = navController,
                            showBackButton = false,
                            showProfileButton = false
                        )
                    }

                    Screen.Detail.route -> {
                        TopBar(
                            title = userName,
                            navController = navController,
                            showBackButton = true,
                            showProfileButton = false
                        )
                    }

                    Screen.Profile.route -> {
                        TopBar(
                            title = stringResource(R.string.menu_profile),
                            navController = navController,
                            showBackButton = true,
                            showProfileButton = false
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (currentRoute != Screen.Detail.route && currentRoute != Screen.Profile.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDetail = { userName ->
                        navController.navigate(Screen.Detail.createRoute(userName))
                    }
                )
            }
            composable(Screen.Star.route) {
                StarScreen(
                    navigateToDetail = { userName ->
                        navController.navigate(Screen.Detail.createRoute(userName))
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("userName") { type = NavType.StringType })
            ) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("userName") ?: ""
                DetailScreen(userName = username, navController = navController)
            }
        }
    }
}