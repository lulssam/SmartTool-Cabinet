package pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.ui.BarraPesquisa
import pfc.a50727a50799.smarttool_cabinet.ui.FilterChip
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AzulReservou
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
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextTitle
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.armarios_icon
import smarttoolcabinet.composeapp.generated.resources.chevron_down
import smarttoolcabinet.composeapp.generated.resources.tool

/**
 * Parte visual do ecrã Ferramentas.
 *
 * Não sabe nada sobre a lógica da aplicação — apenas mostra o que recebe
 * e avisa quando o utilizador faz algo. O estado que é só de UI (secções
 * fechadas, pop-up aberto, campos do formulário) vive aqui em `remember`.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FerramentasGestorScreenContent(
    state: FerramentasUiState,
    onMenuClick: () -> Unit = {},
    onSearchChange: (String) -> Unit = {},
    onFiltroChange: (FiltroFerramenta) -> Unit = {},
    onCriarFerramenta: (nome: String, categoria: String, nArmario: Int?, disponibilidade: String) -> Unit = { _, _, _, _ -> }
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
            var categoriasFechadas by remember { mutableStateOf(emptySet<String>()) }

            var mostrarDialog by remember { mutableStateOf(false) }
            var nome by remember { mutableStateOf("") }
            var categoria by remember { mutableStateOf("") }
            var armarioSel by remember { mutableStateOf<Int?>(null) }
            var estadoInicial by remember { mutableStateOf(EstadoInicial.DISPONIVEL) }

            Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
                TopBar(
                    titulo = "Ferramentas",
                    alertasAtivos = state.alertasAtivos,
                    mostrarAlertas = true,
                    onMenu = onMenuClick
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // titulo + subtitulo + botão
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Ferramentas",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Inventário e rastreabilidade",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }

                            Button(
                                onClick = { mostrarDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "+ Adicionar",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    // barra de pesquisa
                    item {
                        BarraPesquisa(
                            label = "Pesquisar por nome ou código...",
                            query = state.searchQuery,
                            onQueryChange = onSearchChange
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(2.dp))
                    }

                    // chips de filtro
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FiltroFerramenta.entries.forEach { filtro ->
                                FilterChip(
                                    label = filtro.label,
                                    isSelected = state.filtroAtual == filtro,
                                    onClick = { onFiltroChange(filtro) }
                                )
                            }
                        }
                    }

                    // lista agrupada por categoria (secções colapsáveis)
                    if (state.seccoes.isEmpty()) {
                        item {
                            Text(
                                text = "Sem ferramentas para mostrar",
                                fontSize = 14.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    } else {
                        state.seccoes.forEach { seccao ->
                            val fechada = seccao.categoria in categoriasFechadas
                            stickyHeader(key = "header_${seccao.categoria}") {
                                CategoriaHeader(
                                    categoria = seccao.categoria,
                                    total = seccao.ferramentas.size,
                                    fechada = fechada,
                                    onClick = {
                                        categoriasFechadas =
                                            if (seccao.categoria in categoriasFechadas)
                                                categoriasFechadas - seccao.categoria
                                            else
                                                categoriasFechadas + seccao.categoria
                                    }
                                )
                            }
                            if (!fechada) {
                                items(seccao.ferramentas, key = { it.idFerramenta }) { ferramenta ->
                                    FerramentaGestorCard(ferramenta)
                                }
                            }
                        }
                    }
                }
            }

            // pop-up "Adicionar Ferramenta"
            if (mostrarDialog) {
                AdicionarFerramentaDialog(
                    nome = nome,
                    categoria = categoria,
                    categorias = state.categorias,
                    armarios = state.armarios,
                    armarioSelecionado = armarioSel,
                    estadoSelecionado = estadoInicial,
                    onNome = { nome = it },
                    onCategoria = { categoria = it },
                    onArmario = { armarioSel = it },
                    onEstado = { estadoInicial = it },
                    onFechar = { mostrarDialog = false },
                    onAdicionar = {
                        val disponibilidade = when (estadoInicial) {
                            EstadoInicial.DISPONIVEL -> "Disponivel"
                            EstadoInicial.EM_USO -> "Requisitada"
                            EstadoInicial.MANUTENCAO -> "Em Manutencao"
                        }
                        onCriarFerramenta(nome, categoria, armarioSel, disponibilidade)
                        // limpar e fechar
                        nome = ""; categoria = ""; armarioSel = null
                        estadoInicial = EstadoInicial.DISPONIVEL
                        mostrarDialog = false
                    }
                )
            }
        }
    }
}

/**
 * Um card de ferramenta. A cor e o texto da pill (e a cor do nome do detentor)
 * vêm todos da disponibilidade da ferramenta.
 */
@Composable
private fun FerramentaGestorCard(ferramenta: FerramentaUi) {
    val (corFundo, corTexto, label) = when (ferramenta.disponibilidade) {
        DisponibilidadeFerramenta.DISPONIVEL ->
            Triple(TapLightGreen.copy(alpha = 0.2f), TapBrandDark, "Disponível")

        DisponibilidadeFerramenta.REQUISITADA ->
            Triple(AlertOrange.copy(alpha = 0.2f), AlertOrangeText, "Em Uso")

        DisponibilidadeFerramenta.EM_FALTA ->
            Triple(TapAlert.copy(alpha = 0.2f), TapRedText, "Em Falta")

        DisponibilidadeFerramenta.EM_MANUTENCAO ->
            Triple(FieldBg, TextSecondary, "Manutenção")

        DisponibilidadeFerramenta.RESERVADA ->
            Triple(AzulReservou.copy(alpha = 0.2f), AzulReservou, "Reservada")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ícone
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(FieldBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.tool),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            // nome + detalhe (+ detentor, se houver)
            Column(Modifier.weight(1f)) {
                Text(
                    text = ferramenta.nome,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "${ferramenta.codigo} · ${ferramenta.localizacao}",
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    color = TextSecondary
                )
                ferramenta.funcionario?.let { nome ->
                    Text(
                        text = nome,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        color = corTexto,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            // pill de estado
            Text(
                text = label,
                color = corTexto,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(PillShape)
                    .background(corFundo)
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            )
        }
    }
}

/**
 * O cabeçalho de cada categoria. Clicável para abrir/fechar a secção;
 * a setinha muda entre ▾ (aberta) e ▸ (fechada).
 */
@Composable
private fun CategoriaHeader(
    categoria: String,
    total: Int,
    fechada: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ScreenBg)
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$categoria ($total)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextTitle,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = if (fechada) "▸" else "▾",
            fontSize = 14.sp,
            color = TextSecondary
        )
    }
}

@Composable
private fun AdicionarFerramentaDialog(
    nome: String,
    categoria: String,
    categorias: List<String>,
    armarios: List<ArmarioOpcaoUi>,
    armarioSelecionado: Int?,
    estadoSelecionado: EstadoInicial,
    onNome: (String) -> Unit,
    onCategoria: (String) -> Unit,
    onArmario: (Int) -> Unit,
    onEstado: (EstadoInicial) -> Unit,
    onFechar: () -> Unit,
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
                .heightIn(max = 620.dp)
                .imePadding()
        ) {
            Column(Modifier.fillMaxSize()) {
                // cabeçalho: título + subtítulo + X
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Adicionar Ferramenta", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                        Text("Registar nova ferramenta no inventário", fontSize = 13.sp, color = TextSecondary)
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
                    verticalArrangement = Arrangement.spacedBy(19.dp)
                ) {
                    item { CampoTexto("Nome da ferramenta", nome, onNome) }

                    item { CategoriaDropdown(categorias, categoria, onCategoria) }

                    item { Text("Armário", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black) }
                    items(armarios, key = { it.id }) { arm ->
                        ArmarioSelecionavel(arm, arm.id == armarioSelecionado) { onArmario(arm.id) }
                    }

                    item { EstadoInicialSelector(estadoSelecionado, onEstado) }
                }

                Button(
                    onClick = onAdicionar,
                    colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
                    shape = RoundedCornerShape(10.dp),
                    enabled = nome.isNotBlank() && categoria.isNotBlank() && armarioSelecionado != null,
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(48.dp)
                ) {
                    Text("+ Adicionar Ferramenta", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun CampoTexto(label: String, valor: String, onChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
        TextField(
            value = valor, onValueChange = onChange, singleLine = true,
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
private fun CategoriaDropdown(
    categorias: List<String>,
    selecionada: String,
    onSelecionar: (String) -> Unit
) {
    var aberto by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Categoria", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
        Box {
            Row(
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(FieldBg)
                    .clickable { aberto = true }
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selecionada.ifBlank { "Escolher categoria" },
                    color = if (selecionada.isBlank()) TextSecondary else Color.Black,
                    fontSize = 14.sp
                )
                Icon(
                    painter = painterResource(Res.drawable.chevron_down),
                    contentDescription = null
                )
            }
            DropdownMenu(expanded = aberto, onDismissRequest = { aberto = false }) {
                categorias.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat) },
                        onClick = { onSelecionar(cat); aberto = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun ArmarioSelecionavel(arm: ArmarioOpcaoUi, selecionado: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (selecionado) TapLightGreen.copy(alpha = 0.1f) else Color.White)
            .border(
                if (selecionado) 1.5.dp else 1.dp,
                if (selecionado) TapGreenishBlue else CardBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(30.dp).clip(RoundedCornerShape(6.dp)).background(TapGreenishBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.armarios_icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(arm.nome, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Black)
            Text(arm.detalhe, fontSize = 10.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun EstadoInicialSelector(selecionado: EstadoInicial, onSelect: (EstadoInicial) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Estado inicial", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            EstadoInicial.entries.forEach { e ->
                val sel = e == selecionado
                Box(
                    Modifier.weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (sel) TapGreenishBlue.copy(alpha = 0.1f) else Color.White)
                        .border(1.5.dp, if (sel) TapGreenishBlue else CardBorder, RoundedCornerShape(12.dp))
                        .clickable { onSelect(e) }
                        .padding(vertical = 12.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        e.label,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = if (sel) TapGreenishBlue else Color.Black,
                        maxLines = 1
                    )
                }
            }
        }
    }
}


/**
 * Liga o [FerramentasGestorViewModel] ao [FerramentasGestorScreenContent].
 */
@Composable
fun FerramentasGestorScreen(
    viewModel: FerramentasGestorViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    FerramentasGestorScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onSearchChange = viewModel::onSearchChange,
        onFiltroChange = viewModel::onFiltroChange,
        onCriarFerramenta = viewModel::criarFerramenta
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        FerramentasGestorScreenContent(
            state = FerramentasUiState(
                alertasAtivos = 4,
                categorias = listOf("Chaves", "Alicates"),
                seccoes = listOf(
                    SecaoFerramentas(
                        categoria = "Chaves",
                        ferramentas = listOf(
                            FerramentaUi(
                                idFerramenta = 100001, codigo = "0001-00001",
                                nome = "Chave de Caixa 10mm", categoria = "Chaves",
                                localizacao = "Arm. 1",
                                disponibilidade = DisponibilidadeFerramenta.DISPONIVEL,
                                estado = EstadoFerramenta.OPERACIONAL
                            ),
                            FerramentaUi(
                                idFerramenta = 100004, codigo = "0001-00004",
                                nome = "Chave de Parafusos", categoria = "Chaves",
                                localizacao = "Arm. 1",
                                disponibilidade = DisponibilidadeFerramenta.EM_FALTA,
                                estado = EstadoFerramenta.OPERACIONAL,
                                funcionario = "Carlos Gonçalves"
                            )
                        )
                    ),
                    SecaoFerramentas(
                        categoria = "Alicates",
                        ferramentas = listOf(
                            FerramentaUi(
                                idFerramenta = 200002, codigo = "0002-00002",
                                nome = "Alicate de Bico", categoria = "Alicates",
                                localizacao = "Arm. 2",
                                disponibilidade = DisponibilidadeFerramenta.REQUISITADA,
                                estado = EstadoFerramenta.OPERACIONAL,
                                funcionario = "Luísa Sampaio"
                            )
                        )
                    )
                )
            ),
            onMenuClick = {},
            onSearchChange = {},
            onFiltroChange = {}
        )
    }
}