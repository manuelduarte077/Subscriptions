package dev.donmanuel.monthlybill.app.features.categories.presentation.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category
import dev.donmanuel.monthlybill.app.theme.FontSize
import dev.donmanuel.monthlybill.app.theme.parkinsansFont
import dev.donmanuel.monthlybill.app.theme.redHatBoldFont
import dev.donmanuel.monthlybill.app.theme.redHatSemiBoldFont
import org.jetbrains.compose.resources.painterResource

@Composable
fun CategoryBottomSheetContent(
    category: Category, onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(category.icon),
            contentDescription = "${category.name} Icon",
            modifier = Modifier.size(150.dp).padding(bottom = 16.dp),
        )

        Text(
            text = category.name,
            fontFamily = redHatBoldFont(),
            fontSize = FontSize.LARGE,
            color = MaterialTheme.colorScheme.secondary,
            letterSpacing = 1.5.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = category.description,
            fontFamily = parkinsansFont(),
            fontSize = FontSize.EXTRA_REGULAR,
            color = MaterialTheme.colorScheme.secondary,
            letterSpacing = 1.8.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (category.plans.isNotEmpty()) {
            Text(
                text = "Available Plans:",
                fontFamily = redHatSemiBoldFont(),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
            )

            category.plans.forEach { plan ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = plan.name,
                        fontFamily = parkinsansFont(),
                        fontSize = FontSize.EXTRA_REGULAR
                    )
                    Text(
                        text = '$' + plan.price,
                        fontFamily = redHatBoldFont(),
                        fontSize = FontSize.MEDIUM
                    )
                }
            }
        }
    }
}