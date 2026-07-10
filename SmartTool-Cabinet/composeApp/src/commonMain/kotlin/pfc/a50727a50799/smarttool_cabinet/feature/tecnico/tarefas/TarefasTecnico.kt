package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.tarefas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.EstadoTarefa
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.PrioridadeTarefa
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.TarefaUi
import pfc.a50727a50799.smarttool_cabinet.ui.FilterChip
import pfc.a50727a50799.smarttool_cabinet.ui.PillEstado
import pfc.a50727a50799.smarttool_cabinet.ui.PillPrioridade
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FieldBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandDark
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapGreenishBlue
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.check
import smarttoolcabinet.composeapp.generated.resources.chevron_down
import smarttoolcabinet.composeapp.generated.resources.tool
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.tarefas.FiltroTarefa

/**
 * Mostra o conteúdo do ecrã de tarefas do técnico.
 *
 * Dependendo do estado atual, apresenta um indicador de carregamento,
 * uma mensagem de erro ou a lista de tarefas do técnico, permitindo
 * filtrar, expandir e concluir tarefas.
 *
 * @param state Informação necessária para apresentar o estado atual do ecrã.
 * @param onMenuClick Função chamada quando o utilizador abre o menu lateral.
 * @param onFiltroChange Função chamada quando o filtro de tarefas é alterado.
 * @param onToggleExpandir Função chamada quando uma tarefa é expandida ou recolhida.
 * @param onConcluir Função chamada quando uma tarefa é marcada como concluída.
 * @param onMensagemMostrada Função chamada após a apresentação de uma mensagem.
 */
@Composable
private fun TarefasTecnicoScreenContent(
    state: TarefasTecnicoUiState,
    onMenuClick: () -> Unit = {},
    onFiltroChange: (FiltroTarefa) -> Unit = {},
    onToggleExpandir: (String) -> Unit = {},
    onConcluir: (String) -> Unit = {},
    onMensagemMostrada: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // REATIVO: sempre que surge uma mensagem nova, mostra-a no snackbar e limpa.
    LaunchedEffect(state.mensagem) {
        val msg = state.mensagem?.takeIf { it.isNotBlank() } ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(msg)
        onMensagemMostrada()
    }

    Box(Modifier.fillMaxSize()) {

        Column(Modifier.fillMaxSize().background(ScreenBg)) {

            TopBar(titulo = "Tarefas", mostrarAlertas = false, onMenu = onMenuClick)

            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = TapBrandDark)
                }

                state.error != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(state.error, color = MaterialTheme.colorScheme.error)
                }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // cabeçalho
                    item {
                        Column {
                            Text(
                                "As minhas tarefas",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                "Turno ${state.turno} - Hoje",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // cartões de contagem
                    item {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CartaoContagem(Modifier.weight(1f), state.nPendentes, "Pendentes", AlertOrangeText)
                            CartaoContagem(Modifier.weight(1f), state.nEmCurso, "Em curso", TapGreenishBlue)
                            CartaoContagem(Modifier.weight(1f), state.nConcluidas, "Concluídas", TextSecondary)
                        }
                    }

                    // chips de filtro
                    item {
                        Row(
                            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FiltroTarefa.entries.forEach { f ->
                                FilterChip(f.label, state.filtroAtual == f) { onFiltroChange(f) }
                            }
                        }
                    }

                    // lista de tarefas
                    if (state.tarefas.isEmpty()) {
                        item {
                            Text(
                                "Sem tarefas para mostrar.",
                                fontSize = 14.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    } else {
                        items(state.tarefas, key = { it.id }) { tarefa ->
                            TarefaCardTecnico(
                                tarefa = tarefa,
                                expandida = tarefa.id in state.expandidas,
                                onToggle = { onToggleExpandir(tarefa.id) },
                                onConcluir = { onConcluir(tarefa.id) }
                            )
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * Mostra um cartão com a contagem de tarefas.
 *
 * O cartão apresenta o número de tarefas de uma determinada categoria
 * e a respetiva descrição.
 *
 * @param modifier Modificador utilizado para personalizar o cartão.
 * @param valor Valor apresentado no cartão.
 * @param label Descrição da contagem apresentada.
 * @param cor Cor utilizada para destacar o valor.
 */
@Composable
private fun CartaoContagem(modifier: Modifier, valor: Int, label: String, cor: Color) {
    Column(
        modifier = modifier
            .clip(CardShape)
            .background(Color.White)
            .border(1.dp, TextSecondary.copy(alpha = 0.5f), CardShape)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text("$valor", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = cor)
        Text(label, fontSize = 13.sp, color = TextSecondary)
    }
}
/**
 * Mostra um cartão com a informação de uma tarefa.
 *
 * O cartão apresenta o estado da tarefa, a prioridade, a descrição
 * e permite expandir o conteúdo ou marcar a tarefa como concluída.
 *
 * @param tarefa Informação da tarefa apresentada.
 * @param expandida Indica se o cartão se encontra expandido.
 * @param onToggle Função chamada para expandir ou recolher o cartão.
 * @param onConcluir Função chamada quando a tarefa é concluída.
 */
@Composable
private fun TarefaCardTecnico(
    tarefa: TarefaUi,
    expandida: Boolean,
    onToggle: () -> Unit,
    onConcluir: () -> Unit,
) {
    val concluida = tarefa.estado == EstadoTarefa.CONCLUIDA
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, TapGreenishBlue.copy(alpha = 0.2f))
    ) {
        Column(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // cabeçalho (o clique aqui abre/fecha)
            Row(
                Modifier.fillMaxWidth().clickable(onClick = onToggle),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                        .background(TapGreenishBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painterResource(if (concluida) Res.drawable.check else Res.drawable.tool),
                        null, tint = TapGreenishBlue, modifier = Modifier.size(20.dp)
                    )
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        tarefa.titulo, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                        color = Color.Black, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Text(tarefa.quando, fontSize = 13.sp, color = TextSecondary)
                }
                Icon(
                    painterResource(Res.drawable.chevron_down),
                    contentDescription = if (expandida) "Fechar" else "Abrir",
                    tint = TextSecondary,
                    modifier = Modifier.rotate(if (expandida) 180f else 0f)
                )
            }

            // pills
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PillPrioridade(tarefa.prioridade)
                PillEstado(tarefa.estado)
            }

            // corpo que só aparece quando expandida
            AnimatedVisibility(visible = expandida) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    HorizontalDivider(color = CardBorder)
                    if (tarefa.descricao.isNotBlank())
                        Text(tarefa.descricao, fontSize = 15.sp, color = Color.Black)

                    if (tarefa.ferramentas.isNotEmpty()) {
                        Text(
                            "FERRAMENTAS NECESSÁRIAS", fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold, color = TextSecondary
                        )
                        tarefa.ferramentas.forEach { LinhaFerramenta(it) }
                    }


                    if (!concluida) {
                        Button(
                            onClick = onConcluir,
                            colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(49.dp)
                        ) {
                            Text(
                                "Marcar como concluída", color = Color.White,
                                fontWeight = FontWeight.SemiBold, fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
/**
 * Mostra uma linha correspondente a uma ferramenta necessária para a tarefa.
 *
 * Apresenta o nome da ferramenta acompanhado do respetivo ícone.
 *
 * @param nome Nome da ferramenta.
 */
@Composable
private fun LinhaFerramenta(nome: String) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(7.dp)).background(FieldBg)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            painterResource(Res.drawable.tool),
            null,
            tint = TextSecondary,
            modifier = Modifier.size(16.dp)
        )
        Text(nome, fontSize = 12.sp, color = Color.Black)
    }
}

/**
 * Liga o [TarefasTecnicoViewModel] ao [TarefasTecnicoScreenContent].
 *
 * Observa o estado do ViewModel e passa-o para o ecrã.
 * Não contém lógica de UI — apenas faz a ligação.
 *
 * @param viewModel O ViewModel que gere o estado deste ecrã.
 *                  É criado automaticamente pelo Compose se não for fornecido.
 */
@Composable
fun TarefasTecnicoScreen(
    viewModel: TarefasTecnicoViewModel = viewModel(),
    onMenuClick: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    TarefasTecnicoScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onFiltroChange = viewModel::onFiltroChange,
        onToggleExpandir = viewModel::onToggleExpandir,
        onConcluir = viewModel::concluir,
        onMensagemMostrada = viewModel::limparMensagem
    )
}

/**
 * Pré-visualização do ecrã TarefasTecnico para usar durante o desenvolvimento.
 *
 * Usa dados fictícios para simular como o ecrã ficará com informação real.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TarefasTecnicoPreview() {
    AppTheme {
        TarefasTecnicoScreenContent(
            state = TarefasTecnicoUiState(
                turno = "08:00-16:00",
                mensagem = null,
                nPendentes = 0, nEmCurso = 1, nConcluidas = 1,
                expandidas = setOf("3"),
                tarefas = listOf(
                    TarefaUi(
                        "3", "Inspeção Motor A320", "Verificação periódica do motor CFM56-5B",
                        "#0003", "08:00 - Hoje", "João", EstadoTarefa.EM_CURSO,
                        PrioridadeTarefa.ALTA, listOf("Torquímetro 60Nm", "Alicate de Corte")
                    ),
                    TarefaUi(
                        "2", "Calibração de Equipamentos", "…",
                        "#0002", "11:20 - Ontem", "João", EstadoTarefa.CONCLUIDA,
                        PrioridadeTarefa.ALTA, emptyList()
                    )
                )
            )
        )
    }
}

