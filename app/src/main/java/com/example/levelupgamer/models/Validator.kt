package com.example.levelupgamer.models

object Validator {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.length >= 9 && phone.all { it.isDigit() || it == '+' || it == ' ' }
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidPostalCode(code: String): Boolean {
        return code.length >= 4 && code.length <= 10
    }

    fun isValidPrice(price: Double): Boolean {
        return price > 0
    }

    fun isValidStock(stock: Int): Boolean {
        return stock >= 0
    }
}