package pfc.a50727a50799.smarttool_cabinet.di

import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthRepository
import pfc.a50727a50799.smarttool_cabinet.core.auth.FirebaseAuthRepository
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.funcionario.FuncionarioRemoteDataSource

/**
 * No emulador Android, o backend que corre no teu computador é alcançado pelo
 * endereço especial 10.0.2.2 (equivale ao "localhost" da máquina de desenvolvimento).
 */
actual val baseUrl: String = "http://10.0.2.2:8080"

/**
 * No Android usamos o Firebase para confirmar a identidade do utilizador.
 *
 * @param dataSource Quem vai ao backend buscar o cargo do funcionário.
 * @return O repositório de autenticação baseado no Firebase.
 */
actual fun provideAuthRepository(dataSource: FuncionarioRemoteDataSource): AuthRepository =
    FirebaseAuthRepository(dataSource)