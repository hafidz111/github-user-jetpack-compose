package com.example.githubuser.ui.screen.home

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.di.Injection
import com.example.githubuser.ui.ViewModelFactory
import com.example.githubuser.ui.common.UiState
import com.example.githubuser.ui.component.Search
import com.example.githubuser.ui.component.UserCard
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit
) {
    val viewModel: HomeViewModel =
        viewModel(factory = ViewModelFactory(Injection.provideRepository(LocalContext.current)))
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val starUsers by viewModel.starUsers.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        Search(
            query = searchQuery,
            onQueryChange = { query ->
                searchQuery = query
                coroutineScope.launch {
                    viewModel.searchUsers(query)
                }
            }
        )

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
                val users = (uiState as UiState.Success<List<ItemsItem>>).data
                LazyColumn {
                    items(users) { user ->
                        UserCard(
                            userPhoto = user.avatarUrl,
                            userName = user.login,
                            onClick = {
                                navigateToDetail(user.login)
                            },
                            isStar = starUsers.any { it.id == user.id },
                            onStarClick = { viewModel.toggleStar(user) }
                        )
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