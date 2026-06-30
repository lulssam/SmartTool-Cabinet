package pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.ui.FilterChip
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FieldBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.PillShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapAlert
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandDark
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapGreenishBlue
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapRedText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.tool

/**
 * Parte visual do ecrã Tarefas (Gestor).
 *
 * Não sabe nada da lógica — só mostra o `state` que recebe e avisa quando o
 * utilizador interage. Fácil de testar e de pré-visualizar.
 */
@Composable
private fun TarefasGestorScreenContent(
    state: TarefasGestorUiState,
    onMenuClick: () -> Unit = {},
    onFiltroChange: (FiltroTarefa) -> Unit = {},
    onNovaClick: () -> Unit = {}
) {
    when {
        state.isLoading ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        state.error != null ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error, color = MaterialTheme.colorScheme.error)
            }

        else -> {
            Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
                TopBar(
                    titulo = "Tarefas",
                    alertasAtivos = state.alertasAtivos,
                    mostrarAlertas = true,
                    onMenu = onMenuClick
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // título + subtítulo + botão "+ Nova"
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = "Tarefas",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Atribuição de tarefas e ferramentas",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                            Button(
                                onClick = onNovaClick,
                                colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "+ Nova",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // chips de filtro (scroll horizontal em ecrãs estreitos)
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FiltroTarefa.entries.forEach { filtro ->
                                FilterChip(
                                    label = filtro.label,
                                    isSelected = state.filtroAtual == filtro,
                                    onClick = { onFiltroChange(filtro) }
                                )
                            }
                        }
                    }

                    // lista de tarefas
                    if (state.tarefas.isEmpty()) {
                        item {
                            Text(
                                text = "Sem tarefas para mostrar.",
                                fontSize = 14.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    } else {
                        items(state.tarefas, key = { it.id }) { tarefa ->
                            TarefaCard(tarefa)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TarefaCard(t: TarefaUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, corBordaPrioridade(t.prioridade))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // título + pill de estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = t.titulo,
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.width(8.dp))
                PillEstado(t.estado)
            }

            // código · quando
            Text("${t.codigo} · ${t.quando}", fontSize = 13.sp, color = Color.Black)

            // avatar + técnico + pill de prioridade
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(t.tecnico)
                Spacer(Modifier.width(10.dp))
                Text(
                    text = t.tecnico,
                    modifier = Modifier.weight(1f),
                    fontSize = 15.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.width(8.dp))
                PillPrioridade(t.prioridade)
            }

            // chips das ferramentas — quebram para a linha seguinte
            if (t.ferramentas.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    t.ferramentas.forEach { ChipFerramenta(it) }
                }
            }
        }
    }
}

@Composable
private fun PillEstado(estado: EstadoTarefa) {
    val (fundo, texto, label) = when (estado) {
        EstadoTarefa.EM_CURSO -> Triple(TapLightGreen.copy(alpha = 0.2f), TapBrandDark, "Em Curso")
        EstadoTarefa.PENDENTE -> Triple(AlertOrange.copy(alpha = 0.2f), AlertOrangeText, "Pendente")
        EstadoTarefa.CONCLUIDA -> Triple(
            TextSecondary.copy(alpha = 0.2f),
            TextSecondary,
            "Concluída"
        )
    }
    Pill(fundo, texto, label)
}

@Composable
private fun PillPrioridade(prioridade: PrioridadeTarefa) {
    val (fundo, texto, label) = when (prioridade) {
        PrioridadeTarefa.ALTA -> Triple(TapAlert.copy(alpha = 0.2f), TapRedText, "Alta")
        PrioridadeTarefa.NORMAL -> Triple(AlertOrange.copy(alpha = 0.2f), AlertOrangeText, "Normal")
        PrioridadeTarefa.BAIXA -> Triple(TextSecondary.copy(alpha = 0.2f), TextSecondary, "Baixa")
    }
    Pill(fundo, texto, label)
}

@Composable
private fun Pill(fundo: Color, texto: Color, label: String) {
    Text(
        text = label,
        color = texto,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.clip(PillShape).background(fundo)
            .padding(horizontal = 10.dp, vertical = 2.dp)
    )
}

private fun corBordaPrioridade(prioridade: PrioridadeTarefa): Color = when (prioridade) {
    PrioridadeTarefa.ALTA -> TapAlert.copy(alpha = 0.25f)
    PrioridadeTarefa.NORMAL -> AlertOrange.copy(alpha = 0.25f)
    PrioridadeTarefa.BAIXA -> CardBorder
}

@Composable
private fun ChipFerramenta(nome: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(FieldBg)
            .border(0.5.dp, CardBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.tool),
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(14.dp)
        )
        Text(nome, fontSize = 12.sp, color = TextSecondary)
    }
}

@Composable
private fun Avatar(nome: String) {
    Box(
        modifier = Modifier.size(32.dp).clip(CircleShape).background(FieldBg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            iniciais(nome),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary
        )
    }
}

private fun iniciais(nome: String): String {
    val partes = nome.trim().split(" ").filter { it.isNotEmpty() }
    return when {
        partes.isEmpty() -> "?"
        partes.size == 1 -> partes.first().take(1).uppercase()
        else -> "${partes.first().first()}${partes.last().first()}".uppercase()
    }
}

/** Liga o [TarefasGestorViewModel] ao conteúdo. Só observa o estado e reencaminha eventos. */
@Composable
fun TarefasGestorScreen(
    viewModel: TarefasGestorViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    TarefasGestorScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onFiltroChange = viewModel::onFiltroChange,
        onNovaClick = {} // todo: criar nova tarefa
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TarefasGestorPreview() {
    AppTheme {
        TarefasGestorScreenContent(
            state = TarefasGestorUiState(
                alertasAtivos = 4,
                tarefas = listOf(
                    TarefaUi(
                        "3", "Inspeção do motor A320", "#0003", "Hoje: 08:00",
                        "João Ferreira", EstadoTarefa.EM_CURSO, PrioridadeTarefa.ALTA,
                        listOf("Multímetro Digital", "Chave Dinamométrica")
                    ),
                    TarefaUi(
                        "4", "Substituição de parafusos da fuselagem", "#0004", "Hoje: 08:30",
                        "Ana Ferreira", EstadoTarefa.PENDENTE, PrioridadeTarefa.NORMAL,
                        listOf("Chave de Fendas", "Chave Inglesa")
                    ),
                    TarefaUi(
                        "5", "Inspeção dos sistemas aviónicos", "#0005", "Ontem: 14:00",
                        "Rui Almeida", EstadoTarefa.PENDENTE, PrioridadeTarefa.ALTA,
                        listOf("Multímetro Digital", "Alicate Universal", "Berbequim Pneumático")
                    ),
                    TarefaUi(
                        "7", "Calibração de sensores de pressão", "#0007", "27/06: 16:00",
                        "Ana Ferreira", EstadoTarefa.CONCLUIDA, PrioridadeTarefa.BAIXA,
                        listOf("Chave Dinamométrica")
                    )
                )
            ),
            onMenuClick = {},
            onFiltroChange = {},
            onNovaClick = {}
        )
    }
}