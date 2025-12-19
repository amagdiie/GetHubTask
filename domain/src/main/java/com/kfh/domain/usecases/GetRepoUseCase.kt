package com.kfh.domain.usecases

import com.kfh.domain.models.GithubRepo
import com.kfh.domain.repositories.GithubRepository
import javax.inject.Inject

class GetRepoUseCase @Inject constructor(private val githubRepository: GithubRepository) {
    suspend operator fun invoke(page: Int, perPage: Int): List<GithubRepo> {
        return githubRepository.getRepo(page, perPage)
    }
}