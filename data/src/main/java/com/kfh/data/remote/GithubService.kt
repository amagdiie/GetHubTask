package com.kfh.data.remote

import com.kfh.data.local.Preferences
import com.kfh.data.models.GithubRepoDto
import com.kfh.data.models.GithubUserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GithubService @Inject constructor(
    private val preferences: Preferences
) {

    private val client = HttpClient(Android) {
        defaultRequest {
            val token = preferences.getGithubToken()
            header("Authorization", "Bearer $token")
        }

        install(Logging) {
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    suspend fun getProfile(): GithubUserDto {
        return client.get("https://api.github.com/user").body()
    }

    suspend fun getRepos(page: Int = 1, perPage: Int = 30): List<GithubRepoDto> {
        return client.get("https://api.github.com/user/repos") {
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
    }
}