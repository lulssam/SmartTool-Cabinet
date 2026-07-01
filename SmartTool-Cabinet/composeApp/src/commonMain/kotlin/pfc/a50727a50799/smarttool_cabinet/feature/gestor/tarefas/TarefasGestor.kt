package pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.DisponibilidadeFerramenta
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.EstadoFerramenta
import pfc.a50727a50799.smarttool_cabinet.ui.BarraPesquisa
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
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.FerramentaUi

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
    onNovaClick: () -> Unit = {},
    onFecharTarefaClick: () -> Unit = {},
    onTituloChange: (String) -> Unit = {},
    onDescricaoChange: (String) -> Unit = {},
    onTecnicoSelecionado: (Int) -> Unit = {},
    onNovaPrioridade: (PrioridadeTarefa) -> Unit = {},
    onQueryFerramentasChange: (String) -> Unit = {},
    onToggleFerramenta: (Int) -> Unit = {},
    onAdicionarTarefa: () -> Unit = {}
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

                    // chips de filtro
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
                // adicionar nova tarefa
                if (state.mostrarNovaTarefa) {
                    NovaTarefaDialog(
                        state = state,
                        onFechar = onFecharTarefaClick,
                        onTitulo = onTituloChange,
                        onDescricao = onDescricaoChange,
                        onTecnico = onTecnicoSelecionado,
                        onPrioridade = onNovaPrioridade,
                        onQuery = onQueryFerramentasChange,
                        onToggleFerramenta = onToggleFerramenta,
                        onAdicionar = onAdicionarTarefa,
                    )
                }
            }
        }
    }
}

@Composable
private fun NovaTarefaDialog(
    state: TarefasGestorUiState,
    onFechar: () -> Unit,
    onTitulo: (String) -> Unit,
    onDescricao: (String) -> Unit,
    onTecnico: (Int) -> Unit,
    onPrioridade: (PrioridadeTarefa) -> Unit,
    onQuery: (String) -> Unit,
    onToggleFerramenta: (Int) -> Unit,
    onAdicionar: () -> Unit
) {
    Dialog(
        onDismissRequest = onFechar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .widthIn(max = 480.dp)
                .fillMaxHeight(0.9f)
        ) {
            Column(Modifier.fillMaxSize()) {

                // cabeçalho
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Nova Tarefa", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(
                            "Atribuir tarefa e ferramentas a um técnico",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                    Box(
                        Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(TextSecondary.copy(alpha = 0.2f))
                            .clickable(onClick = onFechar),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✕", color = TextSecondary, fontSize = 16.sp)
                    }
                }

                HorizontalDivider()

                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { CampoTexto("Título da Tarefa", state.titulo, onTitulo) }
                    item { CampoTexto("Descrição", state.descricao, onDescricao, minLines = 3) }

                    item { Text("Técnico", fontWeight = FontWeight.SemiBold, fontSize = 14.sp) }
                    items(state.tecnicos, key = { it.id }) { t ->
                        TecnicoSelecionavel(t, t.id == state.tecnicoSelecionado) { onTecnico(t.id) }
                    }

                    item { PrioridadeSelector(state.novaPrioridade, onPrioridade) }

                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                "Ferramentas necessárias",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            BarraPesquisa(
                                label = "Procurar Ferramentas...",
                                query = state.queryFerramentas,
                                onQueryChange = onQuery
                            )
                        }
                    }
                    items(
                        state.ferramentasDisponiveis.filter {
                            it.nome.contains(state.queryFerramentas, ignoreCase = true)
                        },
                        key = { it.idFerramenta }
                    ) { f ->
                        FerramentaCheck(f, f.idFerramenta in state.ferramentasSelecionadas) {
                            onToggleFerramenta(f.idFerramenta)
                        }
                    }
                }

                Button(
                    onClick = onAdicionar,
                    colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(48.dp)
                ) {
                    Text(
                        "+ Adicionar Tarefa",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun FerramentaCheck(f: FerramentaUi, checked: Boolean, onToggle: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .border(1.5.dp, TextSecondary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .clickable(onClick = onToggle).padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Checkbox(checked = checked, onCheckedChange = { onToggle() })
        Icon(
            painterResource(Res.drawable.tool),
            null,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
        Column(Modifier.weight(1f)) {
            Text(f.nome, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
            Text("${f.codigo} · ${f.localizacao}", fontSize = 10.sp, color = TextSecondary)
        }
    }
}


@Composable
private fun CampoTexto(
    label: String,
    valor: String,
    onChange: (String) -> Unit,
    minLines: Int = 1
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
        TextField(
            value = valor, onValueChange = onChange,
            singleLine = minLines == 1, minLines = minLines,
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = FieldBg,
                unfocusedContainerColor = FieldBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun TecnicoSelecionavel(t: TecnicoUi, selecionado: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(if (selecionado) TapLightGreen.copy(0.1f) else Color.White)
            .border(
                if (selecionado) 1.5.dp else 1.dp,
                if (selecionado) TapGreenishBlue else CardBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(t.nome)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(t.nome, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Black)
            Text("Turno ${t.turno}", fontSize = 10.sp, color = TextSecondary)
        }
        if (t.disponivel) Pill(TapLightGreen.copy(0.2f), TapBrandDark, "Disponível")
        else Pill(AlertOrange.copy(0.2f), AlertOrangeText, "Ocupado")
    }
}

@Composable
private fun PrioridadeSelector(
    selecionada: PrioridadeTarefa,
    onSelect: (PrioridadeTarefa) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Prioridade", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PrioridadeTarefa.entries.forEach { p ->
                val sel = p == selecionada
                val cor = corPrioridade(p)
                Box(
                    Modifier.weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (sel) cor.copy(0.1f) else Color.White)
                        .border(
                            1.5.dp,
                            if (sel) cor else CardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelect(p) }.padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        p.label,
                        fontWeight = FontWeight.SemiBold,
                        color = if (sel) cor else Color.Black
                    )
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
        PrioridadeTarefa.NORMAL -> Triple(TapBrandDark.copy(alpha = 0.2f), TapBrandDark, "Normal")
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
    PrioridadeTarefa.NORMAL -> TapBrandDark.copy(alpha = 0.25f)
    PrioridadeTarefa.BAIXA -> CardBorder
}


private fun corPrioridade(p: PrioridadeTarefa): Color = when (p) {
    PrioridadeTarefa.ALTA -> TapRedText
    PrioridadeTarefa.NORMAL -> TapBrandDark
    PrioridadeTarefa.BAIXA -> TextSecondary
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
    onMenuClick: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    TarefasGestorScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onNovaPrioridade = viewModel::onNovaPrioridade,
        onTituloChange = viewModel::onTituloChange,
        onDescricaoChange = viewModel::onDescricaoChange,
        onTecnicoSelecionado = viewModel::onTecnicoSelecionado,
        onToggleFerramenta = viewModel::onToggleFerramenta,
        onQueryFerramentasChange = viewModel::onQueryFerramentasChange,
        onFiltroChange = viewModel::onFiltroChange,
        onNovaClick = viewModel::abrirNovaTarefa,
        onFecharTarefaClick = viewModel::fecharNovaTarefa,
        onAdicionarTarefa = viewModel::onAdicionarTarefa
    )
}

/*
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
*/

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NovaTarefaPreview() {
    AppTheme {
        TarefasGestorScreenContent(
            state = TarefasGestorUiState(
                alertasAtivos = 4,
                mostrarNovaTarefa = true,
                titulo = "Inspeção Motor A320",
                novaPrioridade = PrioridadeTarefa.ALTA,
                tecnicoSelecionado = 4,
                ferramentasSelecionadas = setOf(100001),
                tecnicos = listOf(
                    TecnicoUi(3, "João Ferreira", "08-16:00", disponivel = true),
                    TecnicoUi(4, "Ana Costa", "08-16:00", disponivel = true),
                    TecnicoUi(5, "Carlos Mota", "08-16:00", disponivel = false)
                ),
                ferramentasDisponiveis = listOf(
                    FerramentaUi(
                        100001, "0001-00001", "Chave de Caixa 10mm", "Chaves", "Arm. A1",
                        DisponibilidadeFerramenta.DISPONIVEL, EstadoFerramenta.OPERACIONAL
                    ),
                    FerramentaUi(
                        100002, "0001-00002", "Chave de Caixa 12mm", "Chaves", "Arm. A2",
                        DisponibilidadeFerramenta.DISPONIVEL, EstadoFerramenta.OPERACIONAL
                    ),
                    FerramentaUi(
                        100003, "0001-00003", "Chave de Caixa 14mm", "Chaves", "Arm. B1",
                        DisponibilidadeFerramenta.DISPONIVEL, EstadoFerramenta.OPERACIONAL
                    )
                )
            )
        )
    }
}