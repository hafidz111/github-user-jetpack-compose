package com.example.githubuser.ui.component

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.githubuser.ui.theme.GithubUserTheme

@Composable
fun UserCard(
    userPhoto: String,
    userName: String,
    onClick: () -> Unit,
    isStar: Boolean,
    onStarClick: () -> Unit,
    modifier: Modifier = Modifier,
    showIconStar: Boolean = true
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = userPhoto,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Text(
                text = userName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(3f)
            )
            if (showIconStar) {
                IconButton(
                    onClick = onStarClick,
                    modifier = Modifier.weight(0.8f)
                ) {
                    Icon(
                        imageVector = if (isStar) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = if (isStar) "Unstar" else "Star",
                        tint = if (isStar) Color.Yellow else Color.Gray
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserCardPreview() {
    GithubUserTheme {
        UserCard(
            userPhoto = "https://avatars.githubusercontent.com/u/18?v=4",
            userName = "user1",
            isStar = false,
            onClick = {},
            onStarClick = {}
        )
    }
}