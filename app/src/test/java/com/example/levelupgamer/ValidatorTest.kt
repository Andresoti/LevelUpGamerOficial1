package com.example.levelupgamer

import com.example.levelupgamer.models.Validator
import org.junit.Assert.*
import org.junit.Test

class ValidatorTest {

    @Test
    fun isValidEmail_withValidEmail_returnsTrue() {
        val validEmail = "usuario@example.com"
        val result = Validator.isValidEmail(validEmail)
        assertTrue("Email válido debería retornar true", result)
    }

    @Test
    fun isValidEmail_withInvalidEmail_returnsFalse() {
        val invalidEmails = listOf(
            "usuario",
            "usuario@",
            "@example.com",
            "usuario@example",
            ""
        )

        invalidEmails.forEach { email ->
            val result = Validator.isValidEmail(email)
            assertFalse("Email '$email' debería ser inválido", result)
        }
    }

    @Test
    fun isValidPhone_withValidPhone_returnsTrue() {
        val validPhones = listOf(
            "+56912345678",
            "912345678",
            "22 345 6789"
        )

        validPhones.forEach { phone ->
            val result = Validator.isValidPhone(phone)
            assertTrue("Teléfono '$phone' debería ser válido", result)
        }
    }

    @Test
    fun isValidPhone_withInvalidPhone_returnsFalse() {
        val invalidPhones = listOf(
            "12345",
            "abc123456",
            ""
        )

        invalidPhones.forEach { phone ->
            val result = Validator.isValidPhone(phone)
            assertFalse("Teléfono '$phone' debería ser inválido", result)
        }
    }

    @Test
    fun isValidPassword_withValidPassword_returnsTrue() {
        val validPasswords = listOf(
            "123456",
            "password123",
            "Secure@Pass123"
        )

        validPasswords.forEach { password ->
            val result = Validator.isValidPassword(password)
            assertTrue("Password '$password' debería ser válido", result)
        }
    }

    @Test
    fun isValidPassword_withShortPassword_returnsFalse() {
        val shortPassword = "12345"
        val result = Validator.isValidPassword(shortPassword)
        assertFalse("Password con menos de 6 caracteres debería ser inválido", result)
    }

    @Test
    fun isValidPostalCode_withValidCode_returnsTrue() {
        val validCodes = listOf(
            "7500000",
            "8320000",
            "1234"
        )

        validCodes.forEach { code ->
            val result = Validator.isValidPostalCode(code)
            assertTrue("Código postal '$code' debería ser válido", result)
        }
    }

    @Test
    fun isValidPostalCode_withInvalidCode_returnsFalse() {
        val invalidCodes = listOf(
            "123",
            "12345678901",
            ""
        )

        invalidCodes.forEach { code ->
            val result = Validator.isValidPostalCode(code)
            assertFalse("Código postal '$code' debería ser inválido", result)
        }
    }

    @Test
    fun isValidPrice_withPositivePrice_returnsTrue() {
        val validPrices = listOf(0.01, 100.0, 99999.99)

        validPrices.forEach { price ->
            val result = Validator.isValidPrice(price)
            assertTrue("Precio $price debería ser válido", result)
        }
    }

    @Test
    fun isValidPrice_withZeroOrNegativePrice_returnsFalse() {
        val invalidPrices = listOf(0.0, -10.0, -999.99)

        invalidPrices.forEach { price ->
            val result = Validator.isValidPrice(price)
            assertFalse("Precio $price debería ser inválido", result)
        }
    }

    @Test
    fun isValidStock_withPositiveStock_returnsTrue() {
        val validStocks = listOf(0, 1, 100, 9999)

        validStocks.forEach { stock ->
            val result = Validator.isValidStock(stock)
            assertTrue("Stock $stock debería ser válido", result)
        }
    }

    @Test
    fun isValidStock_withNegativeStock_returnsFalse() {
        val invalidStocks = listOf(-1, -10, -999)

        invalidStocks.forEach { stock ->
            val result = Validator.isValidStock(stock)
            assertFalse("Stock $stock debería ser inválido", result)
        }
    }
}