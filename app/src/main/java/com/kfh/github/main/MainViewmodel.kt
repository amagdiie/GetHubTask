package com.kfh.github.main

import androidx.lifecycle.ViewModel
import com.kfh.domain.usecases.SaveGithubTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val saveGithubTokenUseCase: SaveGithubTokenUseCase
): ViewModel() {

    fun setToken(token: String) = saveGithubTokenUseCase.invoke(token)

}