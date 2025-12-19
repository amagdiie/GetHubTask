package com.kfh.domain.usecases

import com.kfh.domain.repositories.GithubRepository
import javax.inject.Inject

class ClearGithubTokenUseCase @Inject constructor(private val repository: GithubRepository) {
    operator fun invoke() = repository.clearGithubToken()
}