package pfc.a50727a50799.smarttool_cabinet.di

import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthRepository
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.funcionario.FuncionarioRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.auth.iOSAuthRepository

/**
 * No iOS o backend é alcançado pelo IP da máquina de desenvolvimento na rede local.
 * Este valor muda se mudares de rede ou de computador.
 */
actual val baseUrl: String = "http://172.20.10.4:8080" // ip do computador a correr

/**
 * No iOS ainda não há Firebase nativo, por isso usamos uma implementação stub
 * que resolve o cargo a partir do backend (ver [iOSAuthRepository]).
 *
 * @param dataSource Quem vai ao backend buscar o cargo do funcionário.
 * @return O repositório de autenticação usado no iOS.
 */
actual fun provideAuthRepository(dataSource: FuncionarioRemoteDataSource): AuthRepository =
    iOSAuthRepository(dataSource)