package com.kfh.data.models

import androidx.room.Entity
import com.kfh.domain.models.GithubRepo
import com.kfh.domain.models.GithubUser
import kotlinx.serialization.Serializable

@Serializable
@Entity(primaryKeys = ["id"])
data class GithubRepoDto(
    val name: String,
    val private: Boolean,
    val description: String?,
    val watchers: Int,
    val forks: Int
)

fun GithubRepoDto.toDomain(): GithubRepo {
    return GithubRepo(
        name = name,
        private = private,
        description = description,
        watchers = watchers,
        forks = forks
    )
}