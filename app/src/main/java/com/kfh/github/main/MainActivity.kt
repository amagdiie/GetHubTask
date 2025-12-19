package com.kfh.github.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kfh.github.GITHUB_CLIENT_ID
import com.kfh.github.GITHUB_CLIENT_SECRET
import com.kfh.github.github.GithubActivity
import com.kfh.github.ui.theme.KfhGitHubTheme
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewmodel: MainViewmodel by viewModels()
    private lateinit var service: AuthorizationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        service = AuthorizationService(this)

        setContent {
            KfhGitHubTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { githubAuth() }) {
                            Text(text = "Login with Github")
                        }
                    }
                }
            }
        }
    }

    private val launcher = registerForActivityResult(StartActivityForResult()){
        if (it.resultCode == RESULT_OK) {
            val ex = AuthorizationException.fromIntent(it.data!!)
            val result = AuthorizationResponse.fromIntent(it.data!!)

            if (ex != null){
                Log.e("Github Auth", "launcher: $ex")
            } else {
                val secret = ClientSecretBasic(GITHUB_CLIENT_SECRET)
                val tokenRequest = result?.createTokenExchangeRequest()

                service.performTokenRequest(tokenRequest!!, secret) {res, exception ->
                    if (exception != null){
                        Log.e("Github Auth", "launcher: ${exception.error}" )
                    } else {
                        val token = res?.accessToken
                        viewmodel.setToken(token!!)

                        val intent = Intent(this, GithubActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun githubAuth() {
        val redirectUri = Uri.parse("kfhtask://callback")
        val authorizeUri = Uri.parse("https://github.com/login/oauth/authorize")
        val tokenUri = Uri.parse("https://github.com/login/oauth/access_token")

        val config = AuthorizationServiceConfiguration(authorizeUri, tokenUri)
        val request = AuthorizationRequest
            .Builder(config, GITHUB_CLIENT_ID, ResponseTypeValues.CODE, redirectUri)
            .setScopes("user repo admin")
            .build()

        val intent = service.getAuthorizationRequestIntent(request)
        launcher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        service.dispose()
    }
}
