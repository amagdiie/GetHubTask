package com.kfh.github.github

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kfh.domain.models.GithubRepo
import com.kfh.domain.models.GithubUser
import com.kfh.github.main.MainActivity
import com.kfh.github.ui.theme.KfhGitHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GithubActivity : ComponentActivity() {
    private val viewmodel: GithubViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KfhGitHubTheme {
                Surface {
                    GithubProfile()
                }
            }
        }
    }

    @Composable
    private fun GithubProfile() {
        val profile by viewmodel.user
        val filteredRepos = viewmodel.filteredRepos
        val isLoading by viewmodel.isLoading
        val searchQuery by viewmodel.searchQuery

        profile?.let { user ->
            Scaffold(
                topBar = {
                    ProfileTopBar(
                        imageUrl = user.image,
                        username = user.name,
                        onLogoutClick = {
                            viewmodel.clearToken()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            ) { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { ProfileHeader(user) }
                    item { PersonalInfoCard(user) }
                    item {
                        RepoSearchBar(query = searchQuery, onQueryChange = viewmodel::onSearch)
                        Spacer(modifier = Modifier.height(8.dp))
                    }


                    items(filteredRepos) { repo ->
                        RepoItem(repo)
                    }

                    if (isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    if (!isLoading) {
                        item {
                            LaunchedEffect(Unit) {
                                viewmodel.loadRepos()
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun RepoSearchBar(
        query: String,
        onQueryChange: (String) -> Unit
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search repository...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    fun RepoItem(repo: GithubRepo) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = repo.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                if (!repo.description.isNullOrEmpty()) {
                    Text(text = repo.description!!, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "Watchers: ${repo.watchers}")
                    Text(text = "Forks: ${repo.forks}")
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ProfileTopBar(
        imageUrl: String,
        username: String,
        onLogoutClick: () -> Unit
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(text = username)
            },
            navigationIcon = {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "User avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                )
            },
            actions = {
                IconButton(onClick = onLogoutClick) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout"
                    )
                }
            }
        )
    }

    @Composable
    fun ProfileHeader(user: GithubUser) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = user.image,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = user.name,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }

    @Composable
    fun PersonalInfoCard(user: GithubUser) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Personal Info",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                InfoRow("Repositories", user.repos)
                InfoRow("Gists", user.gists)
                InfoRow("Followers", user.followers)
                InfoRow("Following", user.following)
            }
        }
    }

    @Composable
    fun InfoRow(label: String, value: Int) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label)
            Text(text = value.toString())
        }
    }
}