package org.andreasmlbngaol.ymma.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import ymma.composeapp.generated.resources.Res
import ymma.composeapp.generated.resources.agbalumo_regular
import ymma.composeapp.generated.resources.akaya_kanadaka_regular

@OptIn(ExperimentalResourceApi::class)
@Composable
fun displayFontFamily() = FontFamily(
    Font(Res.font.agbalumo_regular, FontWeight.Normal)
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun bodyFontFamily() = FontFamily(
    Font(Res.font.akaya_kanadaka_regular, FontWeight.Normal)
)

@Composable
fun appTypography() = Typography().run {
    val displayFontFamily = displayFontFamily()
    val bodyFontFamily = bodyFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = titleMedium.copy(fontFamily = displayFontFamily),
        titleSmall = titleSmall.copy(fontFamily = displayFontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = labelSmall.copy(fontFamily = bodyFontFamily)

    )

}