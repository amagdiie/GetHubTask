package com.kfh.data.repositories

import com.kfh.data.local.Preferences
import com.kfh.data.models.GithubRepoDto
import com.kfh.data.models.toDomain
import com.kfh.data.remote.GithubService
import com.kfh.domain.models.GithubRepo
import com.kfh.domain.models.GithubUser
import com.kfh.domain.repositories.GithubRepository
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val preferences: Preferences,
    private val githubService: GithubService
): GithubRepository {
    override fun addGithubToken(token: String) {
        preferences.setGithubToken(token)
    }

    override suspend fun getUserInfo(): GithubUser {
        return githubService.getProfile().toDomain()
    }

    override suspend fun getRepo(page: Int, perPage: Int): List<GithubRepo> {
        return githubService.getRepos(page, perPage).map {
            it.toDomain()
        }
    }

    override fun clearGithubToken() {
        preferences.clearGithubToken()
    }
}