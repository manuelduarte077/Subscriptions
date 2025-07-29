package dev.donmanuel.monthlybill.app.utils

/**
 * Utility functions for input validation
 */
object ValidationUtils {
    
    /**
     * Validates if a string can be converted to a valid price (positive number)
     * @param value The string to validate
     * @return true if the string represents a valid positive price, false otherwise
     */
    fun isValidPrice(value: String): Boolean {
        val price = value.toDoubleOrNull()
        return price != null && price > 0
    }
    
    /**
     * Safely converts a string to Double, returning null if conversion fails
     * @param value The string to convert
     * @return The converted Double value or null if conversion fails
     */
    fun parsePrice(value: String): Double? {
        return value.toDoubleOrNull()
    }
    
    /**
     * Validates if a string is not blank and has minimum length
     * @param value The string to validate
     * @param minLength Minimum required length (default: 1)
     * @return true if the string is valid, false otherwise
     */
    fun isValidName(value: String, minLength: Int = 1): Boolean {
        return value.isNotBlank() && value.trim().length >= minLength
    }
} 