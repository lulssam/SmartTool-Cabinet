package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.utilizadores

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
    onNovoClick: (String, String, String, String) -> Unit,
    onAlterarCargo: (Int, String) -> Unit,
    onAlterarTurno: (Int, String) -> Unit,
    onDesativar: (Int) -> Unit,
    onClearError: () -> Unit
) {
    var userEditando by remember { mutableStateOf<UtilizadorListaUi?>(null) }
    var modoEdicao by remember { mutableStateOf<String?>(null) }
    var showCriarDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.error) {
        if (state.error != null) {
            onClearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Gestão de Acessos",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextTitle
                            )
                            Text(
                                "Controlo de permissões",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                        Button(
                            onClick = { showCriarDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("+ Novo", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
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
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
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
                    UtilizadorDetalheCard(
                        user,
                        onEditClick = { userEditando = user; modoEdicao = "OPCOES" },
                        onDeleteClick = { userEditando = user; modoEdicao = "DESATIVAR" })
                }
            }
        }

        if (state.isLoading) {
            Box(
                Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = TapBrandDark) }
        }

        if (userEditando != null) {
            when (modoEdicao) {
                "OPCOES" -> AlertDialog(
                    onDismissRequest = { userEditando = null; modoEdicao = null },
                    title = { Text("Editar ${userEditando?.nome}", fontWeight = FontWeight.Bold) },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { modoEdicao = "CARGO" },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) { Text("Mudar Cargo", color = TapBrandDark) }
                            OutlinedButton(
                                onClick = { modoEdicao = "TURNO" },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) { Text("Mudar Turno", color = TapBrandDark) }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            userEditando = null; modoEdicao = null
                        }) { Text("Cancelar") }
                    },
                    containerColor = Color.White
                )

                "CARGO" -> SelecaoDialog(
                    "Novo Cargo",
                    listOf("GESTOR", "TECNICO", "BACKOFFICE"),
                    onDismiss = { modoEdicao = "OPCOES" }) {
                    onAlterarCargo(
                        userEditando!!.id,
                        it
                    ); userEditando = null; modoEdicao = null
                }

                "TURNO" -> SelecaoDialog(
                    "Novo Turno",
                    listOf("MANHA", "TARDE", "NOITE"),
                    onDismiss = { modoEdicao = "OPCOES" }) {
                    onAlterarTurno(
                        userEditando!!.id,
                        it
                    ); userEditando = null; modoEdicao = null
                }

                "DESATIVAR" -> AlertDialog(
                    onDismissRequest = { userEditando = null; modoEdicao = null },
                    title = { Text("Atenção", fontWeight = FontWeight.Bold) },
                    text = { Text("Desativar o acesso de ${userEditando?.nome}?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                onDesativar(userEditando!!.id); userEditando = null; modoEdicao =
                                null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AlertOrangeText)
                        ) { Text("Desativar") }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            userEditando = null; modoEdicao = null
                        }) { Text("Cancelar") }
                    },
                    containerColor = Color.White
                )
            }
        }

        if (showCriarDialog) {
            CriarUtilizadorDialog(
                onDismiss = { showCriarDialog = false },
                onConfirm = { nome, email, cargo, turno ->
                    onNovoClick(nome, email, cargo, turno)
                    showCriarDialog = false
                }
            )
        }
    }
}

@Composable
fun BOUtilizadoresScreen(
    viewModel: BOUtilizadoresViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    BOUtilizadoresScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onSearchChange = viewModel::onSearchChange,
        onFiltroChange = viewModel::onFiltroChange,
        onNovoClick = viewModel::criarUtilizador,
        onAlterarCargo = viewModel::alterarCargo,
        onAlterarTurno = viewModel::alterarTurno,
        onDesativar = viewModel::desativarConta,
        onClearError = viewModel::limparErro
    )
}

@Composable
fun UtilizadorDetalheCard(
    user: UtilizadorListaUi,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(38.dp).clip(CircleShape)
                        .background(TapLightGreen.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.iniciais,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TapBrandDark
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.nome,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        color = TextTitle
                    )
                    Text(
                        text = user.email,
                        fontSize = 11.sp,
                        lineHeight = 12.sp,
                        color = TextSecondary
                    )
                }
                val (bgColor, textColor) = when (user.cargoTag) {
                    "Gestor" -> Pair(AlertOrange.copy(alpha = 0.15f), AlertOrangeText)
                    "Técnico" -> Pair(TapLightGreen.copy(alpha = 0.2f), TapBrandDark)
                    else -> Pair(TapSurfaceGrey.copy(alpha = 0.3f), TextSecondary)
                }
                Box(
                    modifier = Modifier.clip(PillShape).background(bgColor)
                        .padding(horizontal = 12.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = user.cargoTag,
                        color = textColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = TextSecondary)) {
                                append("Turno: ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append(user.turno)
                            }
                        },
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.size(8.dp).clip(CircleShape)
                            .background(if (user.isAtivo) TapLightGreen else Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (user.isAtivo) "Ativo" else "Inativo",
                        fontSize = 12.sp,
                        color = if (user.isAtivo) TapLightGreen else Color.LightGray
                    )
                }
                if (user.isAtivo) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                painter = painterResource(Res.drawable.editable),
                                contentDescription = null,
                                tint = TextSecondary
                            )

                        }
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                painter = painterResource(Res.drawable.delete),
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
fun SelecaoDialog(
    titulo: String,
    opcoes: List<String>,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titulo, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                opcoes.forEach { opcao ->
                    OutlinedButton(
                        onClick = { onSelect(opcao) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text(opcao, color = TapBrandDark) }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
        containerColor = Color.White
    )
}

@Composable
fun CriarUtilizadorDialog(
    onDismiss: () -> Unit,
    onConfirm: (nome: String, email: String, cargo: String, turno: String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        CriarUtilizadorContent(onFechar = onDismiss, onCriar = onConfirm)
    }
}

@Composable
private fun CriarUtilizadorContent(
    onFechar: () -> Unit,
    onCriar: (nome: String, email: String, cargo: String, turno: String) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var perfil by remember { mutableStateOf(PerfilOpcao.TECNICO) }
    var turno by remember { mutableStateOf(TurnoOpcao.MANHA) }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .widthIn(max = 480.dp)
            .imePadding()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
        ) {
            // titulo + subtitulo + x

            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Novo Utilizador",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextTitle
                    )
                    Text(
                        "Adicionar novo funcionário",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                Box(
                    modifier = Modifier.size(34.dp).clip(RoundedCornerShape(11.dp))
                        .background(TextSecondary.copy(alpha = 0.2f))
                        .clickable(onClick = onFechar),
                    contentAlignment = Alignment.Center
                ) {
                    Text("x", color = TextSecondary, fontSize = 16.sp)
                }
            }

            HorizontalDivider()

            // forms
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                CampoUtilizador("Nome completo", nome, { nome = it })
                CampoUtilizador(
                    "Email empresarial",
                    email,
                    { email = it },
                    placeholder = "nome@tap.pt"
                )

                SeletorSegmentado("Perfil", PerfilOpcao.entries, perfil, { it.label }) {
                    perfil = it
                }
                SeletorSegmentado("Turno", TurnoOpcao.entries, turno, { it.label }) { turno = it }

                Button(
                    onClick = { onCriar(nome, email, perfil.valor, turno.valor) },
                    colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
                    shape = RoundedCornerShape(10.dp),
                    enabled = nome.isNotBlank() && email.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text(
                        "Criar Utilizador",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CampoUtilizador(
    label: String, valor: String, onValor: (String) -> Unit, placeholder: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextTitle)
        TextField(
            value = valor, onValueChange = onValor, singleLine = true,
            placeholder = placeholder?.let { texto ->
                @Composable {
                    Text(
                        texto,
                        color = TextSecondary
                    )
                }
            },
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
private fun <T> SeletorSegmentado(
    titulo: String, opcoes: List<T>, selecionada: T, label: (T) -> String, onSelecionar: (T) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(titulo, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextTitle)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            opcoes.forEach { opcao ->
                val sel = opcao == selecionada
                Box(
                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp))
                        .background(if (sel) TapGreenishBlue.copy(alpha = 0.1f) else Color.White)
                        .border(
                            1.5.dp,
                            if (sel) TapGreenishBlue else CardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelecionar(opcao) }
                        .padding(vertical = 12.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        label(opcao), fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                        color = if (sel) TapGreenishBlue else Color.Black, maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CriarUtilizadorDialogPreview() {
    AppTheme {
        CriarUtilizadorDialog(
            onDismiss = {},
            onConfirm = { _, _, _, _ -> }
        )
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BOUtilizadoresScreenPreview() {
    AppTheme {
        BOUtilizadoresScreenContent(
            state = BOUtilizadoresUiState(
                isLoading = false,               // sem isto mostra só o spinner
                utilizadores = listOf(
                    UtilizadorListaUi(
                        1,
                        "Carlos Gonçalves",
                        "carlos.goncalves@tap.pt",
                        "CG",
                        "Técnico",
                        "Manhã",
                        true
                    ),
                    UtilizadorListaUi(
                        2,
                        "Gonçalo Charneca",
                        "goncalo.charneca@tap.pt",
                        "GC",
                        "Gestor",
                        "Manhã",
                        true
                    ),
                    UtilizadorListaUi(
                        3,
                        "Matilde Valente",
                        "matilde.valente@tap.pt",
                        "MV",
                        "Back Office",
                        "Manhã",
                        true
                    ),
                ),
            ),
            onMenuClick = {}, onSearchChange = {}, onFiltroChange = {},
            onNovoClick = { _, _, _, _ -> }, onAlterarCargo = { _, _ -> },
            onAlterarTurno = { _, _ -> }, onDesativar = {}, onClearError = {},
        )
    }
}*/
