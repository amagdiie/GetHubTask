package com.kfh.domain.models

data class GithubRepo(
    val name: String,
    val private: Boolean,
    val description: String?,
    val watchers: Int,
    val forks: Int
)
