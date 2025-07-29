package dev.donmanuel.monthlybill.app.features.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription
import dev.donmanuel.monthlybill.app.theme.parkinsansFont
import dev.donmanuel.monthlybill.app.theme.redHatBoldFont
import dev.donmanuel.monthlybill.app.utils.Constants
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.parse

@Composable
fun SubscriptionItem(
    subscription: Subscription,
    today: LocalDate
) {
    val (isExpiringSoon, daysToExpire) = remember<Pair<Boolean, Int?>>(subscription.endDate, today) {
        val endDate = subscription.endDate?.let { dateString ->
            try {
                LocalDate.parse(dateString)
            } catch (e: Exception) {
                // Log error for debugging but don't crash the UI
                println("Error parsing date: $dateString, error: ${e.message}")
                null
            }
        }
        val days = endDate?.let { today.daysUntil(it) }
        val highlight = days != null && days in 0..Constants.EXPIRATION_WARNING_DAYS

        highlight to days
    }

    val cardColors = if (isExpiringSoon) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    } else {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }

    val expiringTextColor = MaterialTheme.colorScheme.onTertiaryContainer

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = cardColors
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subscription.name,
                    fontFamily = parkinsansFont(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "${subscription.price} ${subscription.currency}",
                    fontFamily = redHatBoldFont(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${subscription.billingCycle} | ${subscription.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Inicio: ${subscription.startDate}",
                    style = MaterialTheme.typography.bodySmall
                )
                if (!subscription.endDate.isNullOrBlank()) {
                    Text(
                        text = "Fin: ${subscription.endDate}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (isExpiringSoon && daysToExpire != null && daysToExpire >= 0) {
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "¡Vence en $daysToExpire días!",
                        color = expiringTextColor,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}