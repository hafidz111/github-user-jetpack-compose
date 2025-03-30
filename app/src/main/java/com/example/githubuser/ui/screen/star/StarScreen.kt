package com.example.githubuser.ui.screen.star

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.githubuser.di.Injection
import com.example.githubuser.ui.ViewModelFactory
import com.example.githubuser.ui.component.UserCard
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.githubuser.R
import com.example.githubuser.ui.common.UiState

@Composable
fun StarScreen(
    navigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: StarViewModel =
        viewModel(factory = ViewModelFactory(Injection.provideRepository(LocalContext.current)))
    val uiState by viewModel.uiState.collectAsState()
    val starUsers by viewModel.starUsers.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getStarUsers()
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                if (starUsers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_star),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = modifier
                            .padding(top = 8.dp)
                            .fillMaxSize()
                    ) {
                        items(starUsers) { user ->
                            UserCard(
                                userPhoto = user.avatarUrl,
                                userName = user.login,
                                onClick = { navigateToDetail(user.login) },
                                isStar = true,
                                onStarClick = { viewModel.toggleStar(user) }
                            )
                        }
                    }
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as UiState.Error).errorMessage,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}