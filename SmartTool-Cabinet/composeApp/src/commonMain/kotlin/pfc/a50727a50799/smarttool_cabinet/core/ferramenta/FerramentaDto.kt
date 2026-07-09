package pfc.a50727a50799.smarttool_cabinet.core.ferramenta

import kotlinx.serialization.Serializable
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.DisponibilidadeFerramenta
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.EstadoFerramenta
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.FerramentaUi
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.dashboard.FerramentaTecnicoUi
//#my_code
/**
 * Representa a informação de uma ferramenta recebida da API.
 *
 * Contém os dados necessários para identificar a ferramenta
 * e conhecer o seu estado, disponibilidade e localização.
 *
 * @property idRequisicao Identificador da requisição associada à ferramenta, caso exista.
 * @property idFerramenta Identificador único da ferramenta.
 * @property nome Nome da ferramenta.
 * @property estado Estado atual da ferramenta.
 * @property categoria Categoria à qual a ferramenta pertence.
 * @property disponibilidade Indica se a ferramenta está disponível para utilização.
 * @property localizacao Local onde a ferramenta se encontra.
 */
@Serializable
data class FerramentaDto(
    val idRequisicao: Int? = null,
    val idFerramenta: Int,
    val nome: String,
    val estado: String,
    val categoria: String,
    val disponibilidade: String,
    val localizacao: String
)
/**
 * Converte a ferramenta para o modelo utilizado pela interface do técnico.
 *
 * Esta conversão adapta a informação recebida da API para o formato
 * apresentado no ecrã do técnico.
 *
 * @return Objeto utilizado pela interface para apresentar a ferramenta.
 */
fun FerramentaDto.toTecnicoUi(): FerramentaTecnicoUi = FerramentaTecnicoUi(
    id = idFerramenta,
    nome = nome,
    detalhes = "$localizacao, $categoria",
    estado = when (disponibilidade) {
        "Requisitada" -> "Em Uso"
        else -> disponibilidade
    }
)
//#my_code end

/**
 * Junta os dados crus da ferramenta com o detentor (vindo do endpoint /em-falta)
 * e devolve o modelo já pronto para o card do gestor.
 *
 * @param detentor Quem tem a ferramenta agora. Null quando está no armário.
 */
fun FerramentaDto.toGestorUi(detentor: String? = null): FerramentaUi = FerramentaUi(
    idFerramenta = idFerramenta,
    codigo = "${(idFerramenta / 100000).toString().padStart(4, '0')}-" +
            (idFerramenta % 100000).toString().padStart(5, '0'),
    nome = nome,
    categoria = categoria,
    localizacao = localizacao,
    disponibilidade = when (disponibilidade) {
        "Disponivel" -> DisponibilidadeFerramenta.DISPONIVEL
        "Requisitada" -> DisponibilidadeFerramenta.REQUISITADA
        "Reservada" -> DisponibilidadeFerramenta.RESERVADA
        "Em Manutencao" -> DisponibilidadeFerramenta.EM_MANUTENCAO
        else -> DisponibilidadeFerramenta.DISPONIVEL
    },
    estado = when (estado) {
        "Operacional" -> EstadoFerramenta.OPERACIONAL
        "Danificada" -> EstadoFerramenta.DANIFICADA
        "Abatida" -> EstadoFerramenta.ABATIDA
        else -> EstadoFerramenta.OPERACIONAL
    },
    funcionario = detentor
)