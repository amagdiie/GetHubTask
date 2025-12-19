package com.kfh.domain.usecases

import com.kfh.domain.repositories.GithubRepository
import javax.inject.Inject

class SaveGithubTokenUseCase @Inject constructor(private val repository: GithubRepository) {
    operator fun invoke(token: String) = repository.addGithubToken(token)
}