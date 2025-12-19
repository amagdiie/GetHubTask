package com.kfh.github.github

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kfh.domain.models.GithubRepo
import com.kfh.domain.models.GithubUser
import com.kfh.domain.usecases.ClearGithubTokenUseCase
import com.kfh.domain.usecases.GetRepoUseCase
import com.kfh.domain.usecases.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubViewmodel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getRepoUseCase: GetRepoUseCase,
    private val clearGithubTokenUseCase: ClearGithubTokenUseCase
): ViewModel() {

    init {
        getUser()
    }

    // User state
    private val _user = mutableStateOf<GithubUser?>(null)
    val user: State<GithubUser?> = _user

    private fun getUser() {
        viewModelScope.launch {
            val user = getUserInfoUseCase()
            _user.value = user
        }
    }

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _repos = mutableStateOf<List<GithubRepo>>(emptyList())
    val repos: State<List<GithubRepo>> = _repos

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var currentPage = 1
    private val perPage = 3
    private var endReached = false

    fun loadRepos() {
        if (_isLoading.value || endReached) return

        viewModelScope.launch {
            _isLoading.value = true
            val newRepos = getRepoUseCase(page = currentPage, perPage = perPage)
            if (newRepos.isEmpty()) endReached = true
            _repos.value = _repos.value + newRepos
            currentPage++
            _isLoading.value = false
        }
    }


    fun onSearch(query: String) {
        _searchQuery.value = query
    }

    val filteredRepos: List<GithubRepo>
        get() = if (_searchQuery.value.isBlank()) _repos.value
        else _repos.value.filter { it.name.contains(_searchQuery.value, ignoreCase = true) }


    fun clearToken() = clearGithubTokenUseCase()
}