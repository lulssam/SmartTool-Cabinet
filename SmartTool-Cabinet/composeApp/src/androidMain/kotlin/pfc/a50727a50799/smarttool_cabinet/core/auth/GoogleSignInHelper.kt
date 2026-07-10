package pfc.a50727a50799.smarttool_cabinet.core.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import pfc.a50727a50799.smarttool_cabinet.R

/**
 * Abre o ecrã do Android onde a pessoa escolhe com que conta Google quer entrar.
 * Depois de a pessoa escolher, devolve um código (o "idToken") que representa
 * essa conta. Esse código é o que enviamos a seguir ao Firebase para confirmar
 * quem ela é.
 *
 * @param context O contexto do Android, necessário para conseguir mostrar o
 *                seletor de contas e ir buscar as definições da app.
 * @return O código (idToken) da conta Google que a pessoa escolheu.
 * @throws androidx.credentials.exceptions.GetCredentialException Se a pessoa
 *         cancelar a escolha, ou se por algum motivo não for possível obter a conta.
 */
suspend fun obterGoogleIdToken(context: Context): String {
    val credentialManager = CredentialManager.create(context)

    val signInOption = GetSignInWithGoogleOption.Builder(
        serverClientId = context.getString(R.string.default_web_client_id)
    ).build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(signInOption)
        .build()

    val result = credentialManager.getCredential(context, request)
    val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
    return credential.idToken
}