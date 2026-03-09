package pfc.a50727a50799.smarttool_cabinet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform