package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar

// Importações a partir da pasta global
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar

// Importações do Tema
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FieldBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandDark
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextTitle

@Composable
private fun FerramentasScreenContent(
    state: FerramentasUiState,
    onMenuClick: () -> Unit,
    onSearchChange: (String) -> Unit,
    onFiltroChange: (FiltroFerramenta) -> Unit,
    onTemplateClick: (Int) -> Unit
) {
    // Bloco exactly igual ao estilo do Gestor
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
                    titulo = "Ferramentas",
                    mostrarAlertas = false,
                    alertasAtivos = 0,
                    onMenu = onMenuClick
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column {
                            Text(text = "Ferramentas", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextTitle)
                            Text(text = "Consulte e gira as suas ferramentas", fontSize = 14.sp, color = TextSecondary)
                        }
                    }

                    item {
                        Text(text = "Templates diários", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextTitle, modifier = Modifier.padding(top = 8.dp))
                    }

                    items(state.templates) { template ->
                        TemplateCard(template = template, onClick = { onTemplateClick(template.id) })
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        BarraPesquisa(query = state.searchQuery, onQueryChange = onSearchChange)
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
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

                    items(state.ferramentas) { ferramenta ->
                        FerramentaItemCard(ferramenta = ferramenta)
                    }
                }
            }
    }
}

@Composable
fun FerramentasScreen(
    viewModel: FerramentasViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    FerramentasScreenContent(
        state = state,
        onMenuClick = {},
        onSearchChange = viewModel::onSearchChange,
        onFiltroChange = viewModel::onFiltroChange,
        onTemplateClick = viewModel::toggleTemplate
    )
}

// ==========================================
// COMPONENTES DESTA PÁGINA
// ==========================================

@Composable
fun TemplateCard(template: TemplateDiarioUi, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(TapLightGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) { Text("🔧", fontSize = 16.sp) }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = template.nome, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextTitle)
                    Text(text = "${template.totalFerramentas} ferramentas", fontSize = 12.sp, color = TextSecondary)
                }

                Text(
                    text = if (template.isExpanded) "▲" else "▼",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            AnimatedVisibility(visible = template.isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    template.ferramentas.forEach { nomeFerramenta ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp)).background(FieldBg).padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🔧", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = nomeFerramenta, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextTitle)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TapBrandDark),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Requisitar todas ( ${template.totalFerramentas} )", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BarraPesquisa(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Pesquisar ferramentas...", color = TextSecondary) },
        leadingIcon = {
            Text("🔍", fontSize = 16.sp, modifier = Modifier.padding(start = 12.dp))
        },
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
fun FilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) TapLightGreen.copy(alpha = 0.15f) else Color.White
    val borderColor = if (isSelected) TapBrandDark else CardBorder
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
fun FerramentaItemCard(ferramenta: FerramentaListaUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(42.dp).clip(RoundedCornerShape(8.dp)).background(FieldBg),
                    contentAlignment = Alignment.Center
                ) { Text("🔧", fontSize = 18.sp) }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = ferramenta.nome, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextTitle)
                    Text(text = ferramenta.detalhes, fontSize = 11.sp, color = TextSecondary)
                }

                val (bgColor, textColor, label) = when (ferramenta.estado) {
                    EstadoFerramentaLista.DISPONIVEL -> Triple(TapLightGreen.copy(alpha = 0.2f), TapBrandDark, "Disponível")
                    EstadoFerramentaLista.EM_USO -> Triple(AlertOrange.copy(alpha = 0.15f), AlertOrangeText, "Em Uso")
                    EstadoFerramentaLista.MANUTENCAO -> Triple(FieldBg, TextSecondary, "Manutenção")
                }

                Box(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(bgColor).padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = label, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (ferramenta.showDevolverButtons) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = TapLightGreen.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, TapBrandGreen)
                    ) {
                        Text("Devolver", color = TapBrandDark, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = AlertOrange.copy(alpha = 0.1f)),
                        border = BorderStroke(1.dp, AlertOrange)
                    ) {
                        Text("Mau estado", color = AlertOrangeText, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (ferramenta.showRequisitarButton) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TapBrandDark)
                ) {
                    Text("Requisitar ferramenta", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// O Preview injeta os dados mock no Content, deixando o ViewModel totalmente isolado!
@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        FerramentasScreenContent(
            state = FerramentasUiState(
                isLoading = false,
                error = null,
                templates = listOf(
                    TemplateDiarioUi(1, "Inspeção de Rotina A320", 4),
                    TemplateDiarioUi(
                        id = 2,
                        nome = "Manutenção Aviónicos",
                        totalFerramentas = 3,
                        ferramentas = listOf("Torquimetro 60Nm", "Pistola de Calor", "Alicate de Corte"),
                        isExpanded = true
                    )
                ),
                ferramentas = listOf(
                    FerramentaListaUi(
                        id = 1, nome = "Chave de Caixa 10mm", detalhes = "F-001 · Chaves · Arm. 1",
                        estado = EstadoFerramentaLista.DISPONIVEL
                    ),
                    FerramentaListaUi(
                        id = 2, nome = "Alicate de Bico", detalhes = "F-002 · Alicates · Arm. 2",
                        estado = EstadoFerramentaLista.EM_USO, showDevolverButtons = true
                    )
                )
            ),
            onMenuClick = {},
            onSearchChange = {},
            onFiltroChange = {},
            onTemplateClick = {}
        )
    }
}