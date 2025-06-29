package com.example.carcheck.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cars(
    @SerialName("id") val id: Int,
    @SerialName("brand") val brand: String,
    @SerialName("model") val model: String,
    @SerialName("year") val year: Int,
    @SerialName("status") val status: CarStatus,
    @SerialName("right_image_url") val imageRight: String = "",
    @SerialName("left_image_url") val imageLeft: String = ""
){
    @Serializable
    enum class CarStatus {
        @SerialName("OK") OK,
        @SerialName("WARNING") WARNING,
        @SerialName("ERROR") ERROR
    }
}

