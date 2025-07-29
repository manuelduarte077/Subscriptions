package dev.donmanuel.monthlybill.app.utils

/**
 * Application constants
 */
object Constants {
    
    /**
     * Supported currencies for subscriptions
     */
    val SUPPORTED_CURRENCIES = listOf("USD", "EUR", "COP", "MXN", "ARS", "BRL", "CLP", "PEN")
    
    /**
     * Supported billing cycles
     */
    val BILLING_CYCLES = listOf("Mensual", "Trimestral", "Semestral", "Anual")
    
    /**
     * Default category name
     */
    const val DEFAULT_CATEGORY = "General"
    
    /**
     * Minimum name length for validation
     */
    const val MIN_NAME_LENGTH = 1
    
    /**
     * Days before expiration to show warning
     */
    const val EXPIRATION_WARNING_DAYS = 5
    
    /**
     * Date format for parsing
     */
    const val DATE_FORMAT = "YYYY-MM-DD"
    
    /**
     * Error messages
     */
    object ErrorMessages {
        const val INVALID_AMOUNT = "Por favor ingresa un monto válido."
        const val INVALID_DATE_FORMAT = "Formato inválido. Use YYYY-MM-DD"
        const val INVALID_NAME = "El nombre debe tener al menos $MIN_NAME_LENGTH caracteres."
    }
} 