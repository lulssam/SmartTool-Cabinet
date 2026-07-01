package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.menu

import pfc.a50727a50799.smarttool_cabinet.ui.MenuOpcao
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.alert_triangle
import smarttoolcabinet.composeapp.generated.resources.armarios_icon
import smarttoolcabinet.composeapp.generated.resources.clipboard
import smarttoolcabinet.composeapp.generated.resources.clock
import smarttoolcabinet.composeapp.generated.resources.dashboard_icon
import smarttoolcabinet.composeapp.generated.resources.tool
import smarttoolcabinet.composeapp.generated.resources.users

fun opcoesBackOffice(): List<MenuOpcao> = listOf(
    MenuOpcao("dashboard", "Dashboard", Res.drawable.dashboard_icon),
    MenuOpcao("armarios", "Armários", Res.drawable.armarios_icon),
    MenuOpcao("historico", "Histórico", Res.drawable.clock),
    MenuOpcao("utilizadores", "Utilizadores", Res.drawable.users)
)