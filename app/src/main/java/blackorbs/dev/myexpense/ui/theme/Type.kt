package blackorbs.dev.myexpense.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import blackorbs.dev.myexpense.R

val latoFontFamily = FontFamily(
    Font(R.font.lato_bold, FontWeight.Bold),
    Font(R.font.lato_regular, FontWeight.Normal)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = latoFontFamily,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = latoFontFamily
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = latoFontFamily
    ),
    headlineSmall = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = latoFontFamily
    ),
    bodySmall = TextStyle(
        fontFamily = latoFontFamily
    ),
    bodyMedium = TextStyle(
        fontFamily = latoFontFamily
    ),
    labelLarge = TextStyle(
        fontFamily = latoFontFamily
    ),
    labelSmall = TextStyle(
        fontFamily = latoFontFamily
    )
)