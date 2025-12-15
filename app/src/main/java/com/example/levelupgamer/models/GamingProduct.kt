package com.example.levelupgamer.models

import com.google.gson.annotations.SerializedName

data class GamingProduct(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("name")
    var name: String,

    @SerializedName("category")
    var category: String, // Mouse, Teclado, Audífonos, Monitor, etc.

    @SerializedName("brand")
    var brand: String,

    @SerializedName("price")
    var price: Double,

    @SerializedName("description")
    var description: String,

    @SerializedName("stock")
    var stock: Int,

    @SerializedName("specifications")
    var specifications: String // RGB, DPI, Switches, etc.
)