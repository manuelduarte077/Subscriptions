package dev.donmanuel.monthlybill.app.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import monthlybill.composeapp.generated.resources.Res
import monthlybill.composeapp.generated.resources.parkinsans_regular
import monthlybill.composeapp.generated.resources.redhat_bold
import monthlybill.composeapp.generated.resources.redhat_semi_bold
import org.jetbrains.compose.resources.Font

@Composable
fun parkinsansFont() = FontFamily(
    Font(Res.font.parkinsans_regular)
)

@Composable
fun redHatBoldFont() = FontFamily(
    Font(Res.font.redhat_bold)
)

@Composable
fun redHatSemiBoldFont() = FontFamily(
    Font(Res.font.redhat_semi_bold)
)

object FontSize {
    val EXTRA_SMALL = 10.sp
    val SMALL = 12.sp
    val REGULAR = 14.sp
    val EXTRA_REGULAR = 16.sp
    val MEDIUM = 18.sp
    val EXTRA_MEDIUM = 20.sp
    val LARGE = 30.sp
    val EXTRA_LARGE = 40.sp
}
