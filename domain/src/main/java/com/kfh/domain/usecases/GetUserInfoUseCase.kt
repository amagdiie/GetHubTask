package com.kfh.domain.usecases

import com.kfh.domain.models.GithubUser
import com.kfh.domain.repositories.GithubRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val githubRepository: GithubRepository) {
    suspend operator fun invoke(): GithubUser {
       return githubRepository.getUserInfo()
    }
}