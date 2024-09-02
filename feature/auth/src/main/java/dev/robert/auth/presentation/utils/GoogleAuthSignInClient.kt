/*
 * Copyright 2024 Robert Muriithi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.robert.auth.presentation.utils

import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dev.robert.auth.domain.model.GoogleSignResult
import dev.robert.auth.domain.model.GoogleUser
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthSignInClient(
    private val oneTapClient: SignInClient,
    webClientId: String
) {

    private val mAuth = FirebaseAuth.getInstance()
    suspend fun sendIntent(): IntentSender? {
        val result = oneTapClient.beginSignIn(signInRequest).await()
        return result?.pendingIntent?.intentSender
    }

    private val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    fun signOut() {
        try {
            mAuth.signOut()
            oneTapClient.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun revokeAccess() {
        try {
            mAuth.currentUser?.delete()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getUserData() = mAuth.currentUser?.let {
        GoogleUser(
            email = it.email ?: "",
            name = it.displayName ?: "",
            photoUrl = it.photoUrl.toString(),
            id = it.uid
        )
    }
    suspend fun signInWithIntent(intent: Intent): GoogleSignResult {
        return try {
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleCredentials = GoogleAuthProvider.getCredential(credential.googleIdToken, null)
            val user = mAuth.signInWithCredential(googleCredentials).await().user
            GoogleSignResult(
                data = GoogleUser(
                    email = user?.email ?: "",
                    name = user?.displayName ?: "",
                    photoUrl = user?.photoUrl.toString(),
                    id = user?.uid ?: ""
                ),
                errorMsg = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            GoogleSignResult(
                data = null,
                errorMsg = e.message
            )
        }
    }
}
