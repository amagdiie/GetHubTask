package com.kfh.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Preferences @Inject constructor(@ApplicationContext context: Context) {
    private val githubTokenKey = "GITHUB_TOKEN"
    private val preferences = context.getSharedPreferences("KFH_PREFS", Context.MODE_PRIVATE)

    fun getGithubToken() = preferences.getString(githubTokenKey, null)

    fun setGithubToken(token: String?) = preferences.edit().putString(githubTokenKey, token).apply()
    fun clearGithubToken() = preferences.edit().clear()
}