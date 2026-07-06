package pfc.a50727a50799.smarttool_cabinet.feature.backoffice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// Importações do UI Partilhado
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar

// Importações do Tema
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FieldBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandDark
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapGreenishBlue
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapSurfaceGrey
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextTitle

@Composable
private fun BOUtilizadoresScreenContent(
    state: BOUtilizadoresUiState,
    onMenuClick: () -> Unit,
    onSearchChange: (String) -> Unit,
    onFiltroChange: (FiltroUtilizador) -> Unit,
    onNovoClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
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

        else ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ScreenBg)
            ) {
                TopBar(
                    titulo = "Utilizadores",
                    mostrarAlertas = false,
                    alertasAtivos = 0,
                    onMenu = onMenuClick
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Título e Botão "+ Novo"
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Utilizadores", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextTitle)
                                Text(text = "Gestão do pessoal", fontSize = 14.sp, color = TextSecondary)
                            }

                            Button(
                                onClick = onNovoClick,
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("+ Novo", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    // 2. Barra de Pesquisa
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        BarraPesquisaUtilizadores(query = state.searchQuery, onQueryChange = onSearchChange)
                    }

                    // 3. Filtros
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            FiltroUtilizador.entries.forEach { filtro ->
                                FilterChipUtilizador(
                                    label = filtro.label,
                                    isSelected = state.filtroAtual == filtro,
                                    onClick = { onFiltroChange(filtro) }
                                )
                            }
                        }
                    }

                    // 4. Lista de Utilizadores
                    items(state.utilizadores) { user ->
                        UtilizadorDetalheCard(
                            user = user,
                            onEditClick = { onEditClick(user.id) },
                            onDeleteClick = { onDeleteClick(user.id) }
                        )
                    }
                }
            }
    }
}

@Composable
fun BOUtilizadoresScreen(
    viewModel: BOUtilizadoresViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    BOUtilizadoresScreenContent(
        state = state,
        onMenuClick = {},
        onSearchChange = viewModel::onSearchChange,
        onFiltroChange = viewModel::onFiltroChange,
        onNovoClick = {},
        onEditClick = {},
        onDeleteClick = {}
    )
}

// ==========================================
// COMPONENTES DESTA PÁGINA
// ==========================================

@Composable
fun BarraPesquisaUtilizadores(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Pesquisar utilizadores...", color = TextSecondary) },
        leadingIcon = { Text("🔍", fontSize = 16.sp, modifier = Modifier.padding(start = 12.dp)) },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = FieldBg,
            unfocusedContainerColor = FieldBg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun FilterChipUtilizador(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) TapLightGreen.copy(alpha = 0.15f) else Color.White
    val textColor = if (isSelected) TapBrandDark else TextSecondary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = label, color = textColor, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
    }
}

@Composable
fun UtilizadorDetalheCard(
    user: UtilizadorListaUi,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Linha de cima (Iniciais, Nome, Email, Tag do Cargo)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(42.dp).clip(CircleShape).background(TapLightGreen.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = user.iniciais, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TapBrandDark)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = user.nome, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextTitle)
                    Text(text = user.email, fontSize = 11.sp, color = TextSecondary)
                }

                val (bgColor, textColor) = when (user.cargoTag) {
                    "Gestor" -> Pair(AlertOrange.copy(alpha = 0.15f), AlertOrangeText)
                    "Técnico" -> Pair(TapLightGreen.copy(alpha = 0.2f), TapBrandDark)
                    else -> Pair(TapSurfaceGrey.copy(alpha = 0.3f), TextSecondary)
                }

                Box(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(bgColor).padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = user.cargoTag, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Linha de baixo (Turno, Estado Ativo/Inativo, Ações de editar/apagar)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Turno: ${user.turno}", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.width(8.dp))

                    // Bolinha de estado (Verde = Ativo, Cinza = Inativo)
                    Box(
                        modifier = Modifier.size(8.dp).clip(CircleShape).background(if (user.isAtivo) TapLightGreen else Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (user.isAtivo) "Ativo" else "Inativo", fontSize = 12.sp, color = TextSecondary)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "✏️",
                        modifier = Modifier.clickable { onEditClick() },
                        fontSize = 16.sp
                    )
                    Text(
                        text = "🗑️",
                        modifier = Modifier.clickable { onDeleteClick() },
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

// Preview isolado injetando dados falsos diretamente no Content
@Preview(showBackground = true)
@Composable
private fun PreviewBOUtilizadores() {
    AppTheme {
        BOUtilizadoresScreenContent(
            state = BOUtilizadoresUiState(
                isLoading = false,
                error = null,
                utilizadores = listOf(
                    UtilizadorListaUi(1, "Carlos Gonçalves", "carlos.goncalves@tap.pt", "CG", "Técnico", "Manhã", true),
                    UtilizadorListaUi(2, "Gonçalo Charneca", "goncalo.charneca@tap.pt", "GC", "Gestor", "Manhã", true),
                    UtilizadorListaUi(3, "Luísa Sampaio", "luisa.sampaio@tap.pt", "CG", "Técnico", "Manhã", true),
                    UtilizadorListaUi(4, "Tiago Dias", "tiago.dias@tap.pt", "TD", "Técnico", "Manhã", false),
                    UtilizadorListaUi(5, "Matilde Valente", "matilde.valente@tap.pt", "MV", "Back Office", "Manhã", true)
                )
            ),
            onMenuClick = {},
            onSearchChange = {},
            onFiltroChange = {},
            onNovoClick = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}