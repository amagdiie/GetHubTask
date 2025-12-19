package com.kfh.domain.repositories

import com.kfh.domain.models.GithubRepo
import com.kfh.domain.models.GithubUser

interface GithubRepository {
    fun addGithubToken(token: String)
    fun clearGithubToken()
    suspend fun getUserInfo(): GithubUser
    suspend fun getRepo(page: Int, perPage: Int): List<GithubRepo>

}