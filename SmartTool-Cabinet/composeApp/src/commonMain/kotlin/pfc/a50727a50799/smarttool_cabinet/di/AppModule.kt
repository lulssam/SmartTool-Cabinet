package pfc.a50727a50799.smarttool_cabinet.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pfc.a50727a50799.smarttool_cabinet.core.alerta.AlertaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.armario.ArmarioRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthRepository
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.funcionario.FuncionarioRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaRemoteDataSource


/**
 * Endereço base do backend a que a app se liga.
 * Cada plataforma define o seu, porque o servidor é alcançado de forma diferente
 * num emulador Android, num simulador iOS ou num telemóvel real.
 */
expect val baseUrl: String

/**
 * Diz qual é a implementação real da autenticação em cada plataforma:
 * no Android usa o Firebase, no iOS usa o stub. Assim o resto da app só conhece
 * a interface [AuthRepository] e não precisa de saber qual está por trás.
 *
 * @param dataSource Quem vai ao backend buscar o cargo do funcionário.
 * @return O repositório de autenticação adequado à plataforma onde a app corre.
 */
expect fun provideAuthRepository(dataSource: FuncionarioRemoteDataSource): AuthRepository

/**
 * O único sítio onde as dependências da app são construídas e ligadas umas às outras.
 * Cria o cliente HTTP, a fonte de dados e o repositório de autenticação, e guarda-os
 * para serem reutilizados em vez de criados a cada pedido.
 */
object AppModule {

    /**
     * O cliente que faz os pedidos ao backend, já configurado para trocar dados em JSON
     * e para apontar para o [baseUrl]. Criado só na primeira vez que é preciso.
     */
    private val httpClient: HttpClient by lazy { // by lazy garante que cada coisa é criada uma unica vez, na primeira vez que for pedida
        HttpClient {
            install(ContentNegotiation) {json(Json { ignoreUnknownKeys = true })}
            defaultRequest { url(baseUrl) }
        }
    }

    /** A fonte que vai ao backend buscar os dados dos funcionários (usa o [httpClient]). */
    private val funcionarioRemoteDataSource by lazy { FuncionarioRemoteDataSource(httpClient) }

    /** Repositorio de autenticação pronto a usar*/
    val authRepository: AuthRepository by lazy { provideAuthRepository(funcionarioRemoteDataSource) }

    /** A fonte que vai ao backend buscar as ferramentas (usa o [httpClient]). */
    val ferramentaRemoteDataSource by lazy { FerramentaRemoteDataSource(httpClient) }

    /** A fonte que vai ao backend buscar os armários (usa o [httpClient]). */
    val armarioRemoteDataSource by lazy { ArmarioRemoteDataSource(httpClient) }

    /** A fonte que vai ao backend buscar os alertas (usa o [httpClient]). */
    val alertaRemoteDataSource by lazy { AlertaRemoteDataSource(httpClient) }

}