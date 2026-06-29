package pfc.a50727a50799.smarttool_cabinet.feature.gestor.menu

import pfc.a50727a50799.smarttool_cabinet.ui.MenuOpcao
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.alert_triangle
import smarttoolcabinet.composeapp.generated.resources.armarios_icon
import smarttoolcabinet.composeapp.generated.resources.clipboard
import smarttoolcabinet.composeapp.generated.resources.clock
import smarttoolcabinet.composeapp.generated.resources.dashboard_icon
import smarttoolcabinet.composeapp.generated.resources.tool

/**
 * As opções do menu lateral para o perfil de Gestor. O badge dos alertas é
 * dinâmico, por isso isto é uma função (recebe o número) e não uma constante.
 */
fun opcoesGestor(alertasAtivos: Int): List<MenuOpcao> = listOf(
    MenuOpcao("dashboard", "Dashboard", Res.drawable.dashboard_icon),
    MenuOpcao("tarefas", "Tarefas", Res.drawable.clipboard),
    MenuOpcao("armarios", "Armários", Res.drawable.armarios_icon),
    MenuOpcao("ferramentas", "Ferramentas", Res.drawable.tool),
    MenuOpcao("alertas", "Alertas", Res.drawable.alert_triangle, badge = alertasAtivos),
    MenuOpcao("historico", "Histórico", Res.drawable.clock)
)