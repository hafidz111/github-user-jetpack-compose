package com.example.githubuser.ui.screen.detail

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.CircularProgressIndicator
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Tab
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRow
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.di.Injection
import com.example.githubuser.ui.ViewModelFactory
import com.example.githubuser.ui.common.UiState
import com.example.githubuser.ui.component.UserCard


@Composable
fun DetailScreen(
    userName: String,
    navController: NavController,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository(
                LocalContext.current
            )
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val followersState by viewModel.followers.collectAsState()
    val followingState by viewModel.following.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(userName) {
        viewModel.getUserDetail(userName)
        viewModel.getUserFollowers(userName)
        viewModel.getUserFollowing(userName)
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
                val user = (uiState as UiState.Success<DetailUserResponse>).data
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(user.avatarUrl),
                        contentDescription = user.login,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = user.login,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    val tabs = listOf("Followers", "Following")
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentColor = Color.White
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(title) },
                            )
                        }
                    }

                    when (selectedTabIndex) {
                        0 -> FollowerList(followersState, navigateToDetail = { user ->
                            navController.navigate("detail/$user")
                        })

                        1 -> FollowingList(followingState, navigateToDetail = { user ->
                            navController.navigate("detail/$user")
                        })
                    }
                }
            }

            is UiState.Error -> {
                val errorMessage = (uiState as UiState.Error).errorMessage
                Log.e("DetailScreen", "Error: $errorMessage")
                Text(text = errorMessage, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

}

@Composable
fun FollowerList(
    followersState: UiState<List<ItemsItem>>,
    navigateToDetail: (String) -> Unit,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository(LocalContext.current)
        )
    )
) {
    val starUsers by viewModel.starUsers.collectAsState()
    when (followersState) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Success -> LazyColumn {
            items(followersState.data) { follower ->
                UserCard(
                    userPhoto = follower.avatarUrl, userName = follower.login, onClick = {
                        navigateToDetail(follower.login)
                    },
                    isStar = starUsers.any { it.id == follower.id },
                    onStarClick = { },
                    showIconStar = false
                )
            }
        }

        is UiState.Error -> Text(text = followersState.errorMessage)
    }
}

@Composable
fun FollowingList(
    followingState: UiState<List<ItemsItem>>,
    navigateToDetail: (String) -> Unit,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository(LocalContext.current)
        )
    )
) {
    val starUsers by viewModel.starUsers.collectAsState()
    when (followingState) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Success -> LazyColumn {
            items(followingState.data) { following ->
                UserCard(
                    userPhoto = following.avatarUrl, userName = following.login, onClick = {
                        navigateToDetail(following.login)
                    },
                    isStar = starUsers.any { it.id == following.id },
                    onStarClick = { },
                    showIconStar = false
                )
            }
        }

        is UiState.Error -> Text(text = followingState.errorMessage)
    }
}