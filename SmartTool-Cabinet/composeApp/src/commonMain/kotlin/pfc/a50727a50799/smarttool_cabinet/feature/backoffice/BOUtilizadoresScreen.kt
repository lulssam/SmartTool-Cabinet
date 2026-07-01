package pfc.a50727a50799.smarttool_cabinet.feature.backoffice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.ui.BarraPesquisa
import pfc.a50727a50799.smarttool_cabinet.ui.FilterChip
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.theme.*
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.delete
import smarttoolcabinet.composeapp.generated.resources.editable

@Composable
private fun BOUtilizadoresScreenContent(
    state: BOUtilizadoresUiState,
    onMenuClick: () -> Unit,
    onSearchChange: (String) -> Unit,
    onFiltroChange: (FiltroUtilizador) -> Unit,
    // 1. ALTERADO: Agora aceita os 4 campos do formulário
    onNovoClick: (String, String, String, String) -> Unit,
    onAlterarCargo: (Int, String) -> Unit,
    onAlterarTurno: (Int, String) -> Unit,
    onDesativar: (Int) -> Unit,
    onClearError: () -> Unit
) {
    var userEditando by remember { mutableStateOf<UtilizadorListaUi?>(null) }
    var modoEdicao by remember { mutableStateOf<String?>(null) }
    // 2. ADICIONADO: Variável para controlar quando o pop-up de criar aparece
    var showCriarDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.error) {
        if (state.error != null) {
            onClearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(titulo = "Utilizadores", mostrarAlertas = false, alertasAtivos = 0, onMenu = onMenuClick)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Gestão de Acessos", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextTitle)
                            Text("Controlo de permissões e turnos", fontSize = 14.sp, color = TextSecondary)
                        }
                        // 3. ALTERADO: O botão agora muda a variável para mostrar o pop-up
                        Button(
                            onClick = { showCriarDialog = true },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TapBrandDark),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("+ Novo", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                item {
                    BarraPesquisa(
                        label = "Pesquisar por nome ou email...",
                        query = state.searchQuery,
                        onQueryChange = onSearchChange
                    )
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FiltroUtilizador.entries.forEach { filtro ->
                            FilterChip(
                                label = filtro.label,
                                isSelected = state.filtroAtual == filtro,
                                onClick = { onFiltroChange(filtro) }
                            )
                        }
                    }
                }

                items(state.utilizadores) { user ->
                    UtilizadorDetalheCard(user, onEditClick = { userEditando = user; modoEdicao = "OPCOES" }, onDeleteClick = { userEditando = user; modoEdicao = "DESATIVAR" })
                }
            }
        }

        if (state.isLoading) {
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = TapBrandDark) }
        }

        if (userEditando != null) {
            when (modoEdicao) {
                "OPCOES" -> AlertDialog(
                    onDismissRequest = { userEditando = null; modoEdicao = null },
                    title = { Text("Editar ${userEditando?.nome}", fontWeight = FontWeight.Bold) },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { modoEdicao = "CARGO" }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) { Text("Mudar Cargo", color = TapBrandDark) }
                            OutlinedButton(onClick = { modoEdicao = "TURNO" }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) { Text("Mudar Turno", color = TapBrandDark) }
                        }
                    },
                    confirmButton = { TextButton(onClick = { userEditando = null; modoEdicao = null }) { Text("Cancelar") } },
                    containerColor = Color.White
                )
                "CARGO" -> SelecaoDialog("Novo Cargo", listOf("GESTOR", "TECNICO", "BACKOFFICE"), onDismiss = { modoEdicao = "OPCOES" }) { onAlterarCargo(userEditando!!.id, it); userEditando = null; modoEdicao = null }
                "TURNO" -> SelecaoDialog("Novo Turno", listOf("MANHA", "TARDE", "NOITE"), onDismiss = { modoEdicao = "OPCOES" }) { onAlterarTurno(userEditando!!.id, it); userEditando = null; modoEdicao = null }
                "DESATIVAR" -> AlertDialog(
                    onDismissRequest = { userEditando = null; modoEdicao = null },
                    title = { Text("Atenção", fontWeight = FontWeight.Bold) },
                    text = { Text("Desativar o acesso de ${userEditando?.nome}?") },
                    confirmButton = { Button(onClick = { onDesativar(userEditando!!.id); userEditando = null; modoEdicao = null }, colors = ButtonDefaults.buttonColors(containerColor = AlertOrangeText)) { Text("Desativar") } },
                    dismissButton = { TextButton(onClick = { userEditando = null; modoEdicao = null }) { Text("Cancelar") } },
                    containerColor = Color.White
                )
            }
        }

        // 4. ADICIONADO: Se a variável for true, desenha o formulário que criaste
        if (showCriarDialog) {
            CriarUtilizadorDialog(
                onDismiss = { showCriarDialog = false },
                onConfirm = { nome, email, cargo, turno ->
                    onNovoClick(nome, email, cargo, turno) // Envia para o ViewModel
                    showCriarDialog = false // Fecha o pop-up
                }
            )
        }
    }
}

@Composable
fun BOUtilizadoresScreen(viewModel: BOUtilizadoresViewModel = viewModel(), onMenuClick: () -> Unit = {}) {
    val state by viewModel.state.collectAsState()

    BOUtilizadoresScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onSearchChange = viewModel::onSearchChange,
        onFiltroChange = viewModel::onFiltroChange,
        // 5. ALTERADO: Aqui ligamos o botão à função que já tinhas no ViewModel
        onNovoClick = viewModel::criarUtilizador,
        onAlterarCargo = viewModel::alterarCargo,
        onAlterarTurno = viewModel::alterarTurno,
        onDesativar = viewModel::desativarConta,
        onClearError = viewModel::limparErro
    )
}

@Composable
fun UtilizadorDetalheCard(user: UtilizadorListaUi, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, CardBorder)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(42.dp).clip(CircleShape).background(TapLightGreen.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) { Text(text = user.iniciais, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TapBrandDark) }
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
                Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(bgColor).padding(horizontal = 10.dp, vertical = 4.dp)) { Text(text = user.cargoTag, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Turno: ${user.turno}", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(if (user.isAtivo) TapLightGreen else Color.LightGray))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (user.isAtivo) "Ativo" else "Inativo", fontSize = 12.sp, color = TextSecondary)
                }
                if (user.isAtivo) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        IconButton (onClick = onEditClick){
                           Icon ( painter = painterResource(Res.drawable.editable),
                               contentDescription = null,
                               tint = TextSecondary
                           )

                        }
                        IconButton (onClick = onDeleteClick){
                            Icon ( painter = painterResource(Res.drawable.delete),
                                contentDescription = null,
                                tint = TapRedText
                            )

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelecaoDialog(titulo: String, opcoes: List<String>, onDismiss: () -> Unit, onSelect: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss, title = { Text(titulo, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                opcoes.forEach { opcao ->
                    OutlinedButton(onClick = { onSelect(opcao) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) { Text(opcao, color = TapBrandDark) }
                }
            }
        },
        confirmButton = {}, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }, containerColor = Color.White
    )
}

@Composable
fun CriarUtilizadorDialog(
    onDismiss: () -> Unit,
    onConfirm: (nome: String, email: String, cargo: String, turno: String) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("TECNICO") }
    var turno by remember { mutableStateOf("MANHA") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo Utilizador", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome Completo") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email da TAP") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Cargo:", fontSize = 12.sp, color = TextSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("GESTOR", "TECNICO", "BACKOFFICE").forEach { c ->
                        val isSelected = cargo == c
                        OutlinedButton(
                            onClick = { cargo = c },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) TapBrandDark.copy(alpha = 0.1f) else Color.Transparent,
                                contentColor = if (isSelected) TapBrandDark else Color.Gray
                            ),
                            border = BorderStroke(1.dp, if (isSelected) TapBrandDark else Color.LightGray),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            modifier = Modifier.height(32.dp)
                        ) { Text(c, fontSize = 10.sp) }
                    }
                }

                Text("Turno:", fontSize = 12.sp, color = TextSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("MANHA", "TARDE", "NOITE").forEach { t ->
                        val isSelected = turno == t
                        OutlinedButton(
                            onClick = { turno = t },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) TapLightGreen.copy(alpha = 0.2f) else Color.Transparent,
                                contentColor = if (isSelected) TapBrandDark else Color.Gray
                            ),
                            border = BorderStroke(1.dp, if (isSelected) TapBrandDark else Color.LightGray),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            modifier = Modifier.height(32.dp)
                        ) { Text(t, fontSize = 10.sp) }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nome, email, cargo, turno) },
                colors = ButtonDefaults.buttonColors(containerColor = TapBrandDark),
                enabled = nome.isNotBlank() && email.isNotBlank()
            ) { Text("Criar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = Color.Gray) }
        },
        containerColor = Color.White
    )
}