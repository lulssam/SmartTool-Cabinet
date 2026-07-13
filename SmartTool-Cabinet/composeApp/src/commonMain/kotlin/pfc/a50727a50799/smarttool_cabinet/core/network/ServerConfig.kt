package pfc.a50727a50799.smarttool_cabinet.core.network

import pfc.a50727a50799.smarttool_cabinet.di.defaultBaseUrl

/**
 * Guarda em memória o endereço do backend que a aplicação está a
 * usar agora. Arranca com o valor por omissão da plataforma e pode
 * ser mudado em tempo de execução a partir da interface. Como o
 * defaultRequest do HttpClient lê isto a cada pedido, trocar
 * aqui basta para os próximos pedidos irem para o novo IP ⇾ sem
 * recriar o cliente
 * */
object ServerConfig {
    var baseUrl: String = defaultBaseUrl
}