package dev.donmanuel.monthlybill.app.features.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.donmanuel.monthlybill.app.features.bill.data.model.Subscription
import dev.donmanuel.monthlybill.app.features.categories.domain.entities.Category
import dev.donmanuel.monthlybill.app.theme.parkinsansFont
import dev.donmanuel.monthlybill.app.theme.redHatBoldFont
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import org.jetbrains.compose.resources.painterResource

/**
 * Un Composable que muestra la información de una sola suscripción en una Card.
 * Se adapta a los temas claro y oscuro.
 *
 * @param subscription El objeto de datos de la suscripción a mostrar.
 * @param categoryIcon El recurso del ícono para la categoría de la suscripción.
 * @param today La fecha actual para calcular la expiración.
 */
@Composable
fun SubscriptionItem(
    subscription: Subscription,
    category: Category,
    today: LocalDate
) {
    val (isExpiringSoon, daysToExpire) = remember<Pair<Boolean, Int?>>(subscription.endDate, today) {
        val endDate = subscription.endDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        val days = endDate?.let { today.daysUntil(it) }
        val highlight = days != null && days in 0..5
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
            Icon(
                painter = painterResource(category.icon),
                contentDescription = subscription.category,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 16.dp)
            )

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