package com.example.levelupgamer.models

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("email")
    var email: String,

    @SerializedName("password")
    var password: String,

    @SerializedName("fullName")
    var fullName: String,

    @SerializedName("phone")
    var phone: String,

    @SerializedName("address")
    var address: String,

    @SerializedName("city")
    var city: String,

    @SerializedName("postalCode")
    var postalCode: String,

    @SerializedName("country")
    var country: String
)

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)