package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.menu

import pfc.a50727a50799.smarttool_cabinet.ui.MenuOpcao
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.clipboard
import smarttoolcabinet.composeapp.generated.resources.clock
import smarttoolcabinet.composeapp.generated.resources.dashboard_icon
import smarttoolcabinet.composeapp.generated.resources.tool

fun opcoesTecnico(): List<MenuOpcao> = listOf(
    MenuOpcao("dashboard", "Dashboard", Res.drawable.dashboard_icon),
    MenuOpcao("ferramentas", "Ferramentas", Res.drawable.tool),
    MenuOpcao("historico", "Histórico", Res.drawable.clock),
    MenuOpcao("tarefas", "Tarefas", Res.drawable.clipboard)
)