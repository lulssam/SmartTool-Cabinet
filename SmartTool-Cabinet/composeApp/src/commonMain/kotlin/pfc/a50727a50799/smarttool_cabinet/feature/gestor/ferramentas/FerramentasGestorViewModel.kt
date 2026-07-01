package pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.alerta.AlertaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.toGestorUi
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

/**
 * Gere o estado e a lógica do ecrã Ferramentas.
 *
 * Vai buscar os dados necessários ao backend e guarda-os no estado
 * para o ecrã mostrar. O ecrã nunca fala diretamente com o backend —
 * passa sempre por aqui.
 */
class FerramentasGestorViewModel(
    private val ferramentas: FerramentaRemoteDataSource,
    private val alertas: AlertaRemoteDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(FerramentasUiState(isLoading = true))

    /**
     * O estado atual do ecrã, disponível para o Composable observar.
     * Só o ViewModel pode alterar este valor — o ecrã apenas o lê.
     */
    val state: StateFlow<FerramentasUiState> = _state.asStateFlow()


    // lista completa de ferramentas vinda do backend
    private var todasFerramentas: List<FerramentaUi> = emptyList()

    init {
        carregar()
    }

    /**
     * Vai buscar os dados ao backend e atualiza o estado do ecrã.
     *
     * Mostra um indicador de carregamento enquanto espera,
     * e um erro se algo correr mal.
     */
    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // inventario
            val lista = when (val r = ferramentas.getFerramentas()) {
                is ApiResult.Success -> r.data
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = mensagem(r.error)) }
                    return@launch
                }
            }

            // quem tem cada ferramenta
            val porDetentor = when (val r = ferramentas.getEmFalta()) {
                is ApiResult.Success -> r.data.associate { it.idFerramenta to it.detentor }
                is ApiResult.Error -> emptyMap()
            }

            // alertas
            val nAlertas = when (val r = alertas.getAlertas()) {
                is ApiResult.Success -> r.data.size
                is ApiResult.Error -> 0
            }

            // juntar tudo: cada ferramenta recebe o seu detentor (ou null)
            todasFerramentas = lista.map { dto ->
                dto.toGestorUi(detentor = porDetentor[dto.idFerramenta])
            }

            //guardar campos
            _state.update { it.copy(isLoading = false, alertasAtivos = nAlertas) }

            recomputarSeccoes()
        }
    }

    /**
     * Atualiza o texto da pesquisa (o ecrã chama isto a cada tecla) e recalcula
     * as secções com o novo texto.
     *
     * @param query Texto introduzido pelo utilizador.
     */
    fun onSearchChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        recomputarSeccoes()
    }

    /**
     * Troca o chip de filtro selecionado e recalcula as secções.
     *
     * @param filtro Chip selecionado.
     */
    fun onFiltroChange(filtro: FiltroFerramenta) {
        _state.update { it.copy(filtroAtual = filtro) }
        recomputarSeccoes()
    }

    /**
     * Aplica a pesquisa e o chip atuais à lista crua e reconstrói as secções
     * agrupadas por categoria. Corre no arranque e sempre que a pesquisa ou o
     * filtro mudam — nunca vai à rede, só reorganiza o que já está em memória.
     */
    private fun recomputarSeccoes() {
        val query = _state.value.searchQuery
        val filtro = _state.value.filtroAtual

        val seccoes = todasFerramentas
            // pesquisa por nome OU codigo
            .filter { f ->
                query.isBlank() ||
                        f.nome.contains(query, ignoreCase = true) ||
                        f.codigo.contains(query, ignoreCase = true)
            }
            // chip selecionado
            .filter { f ->
                when (filtro) {
                    FiltroFerramenta.TODAS -> true
                    FiltroFerramenta.DISPONIVEL -> f.disponibilidade == DisponibilidadeFerramenta.DISPONIVEL
                    FiltroFerramenta.EM_USO -> f.disponibilidade == DisponibilidadeFerramenta.REQUISITADA
                    FiltroFerramenta.EM_FALTA -> f.disponibilidade == DisponibilidadeFerramenta.EM_FALTA
                    FiltroFerramenta.MANUTENCAO -> f.disponibilidade == DisponibilidadeFerramenta.EM_MANUTENCAO
                    FiltroFerramenta.RESERVADA -> f.disponibilidade == DisponibilidadeFerramenta.RESERVADA
                }
            }
            // agrupar por categoria e transformar em secções
            .groupBy { it.categoria }
            .map { (categoria, ferramentas) -> SecaoFerramentas(categoria, ferramentas) }

        _state.update { it.copy(seccoes = seccoes) }
    }

    /**
     * Transforma um erro do backend numa frase legível para o utilizador.
     *
     * @param erro O erro devolvido pelo backend.
     * @return Uma mensagem em português para mostrar no ecrã.
     */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}