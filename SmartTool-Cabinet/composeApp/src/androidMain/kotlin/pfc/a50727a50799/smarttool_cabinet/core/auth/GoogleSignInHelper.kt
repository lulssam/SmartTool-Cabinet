package pfc.a50727a50799.smarttool_cabinet.core.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import pfc.a50727a50799.smarttool_cabinet.R

/** Mostra o seletor de contas Google e devolve o idToken dessa conta. */
suspend fun obterGoogleIdToken(context: Context): String {
    val credentialManager = CredentialManager.create(context)

    val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(context.getString(R.string.default_web_client_id))
        .setFilterByAuthorizedAccounts(false)   // false = mostra todas as contas (1º login)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    val result = credentialManager.getCredential(context, request)
    val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
    return credential.idToken
}