package pfc.a50727a50799.smarttool_cabinet.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val SmartToolColors = lightColorScheme(
    primary = TapBrandDark,        onPrimary = Color.White,
    secondary = TapBrandGreen,     onSecondary = Color.White,
    tertiary = TapAccent,
    error = TapAlert,              onError = Color.White,
    background = Color.White,      onBackground = TextTitle,
    surface = Color.White,         onSurface = TextTitle,
    surfaceVariant = TapSurfaceGrey, onSurfaceVariant = TextSecondary
)

// formas reutilizáveis — aplica-as por componente
val PillShape  = RoundedCornerShape(28.dp)  // botões em pílula
val CardShape  = RoundedCornerShape(20.dp)  // cartões
val FieldShape = RoundedCornerShape(13.dp)  // campos de texto

/** O tema da app: aplica as cores e a tipografia da TAP a tudo o que estiver lá dentro. */
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SmartToolColors,
        typography = Typography(),  // default por agora; afinamos depois
        content = content
    )
}