package com.example.carcheck.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CarNode(
    @SerialName("id") val id: Int,
    @SerialName("car_id") val carId: Int,
    @SerialName("name") val name: String,
    @SerialName("category") val category: NodeCategory,
    @SerialName("status") val status: NodeStatus
) {
    @Serializable
    enum class NodeCategory {
        @SerialName("ENGINE")
        ENGINE,
        @SerialName("SUSPENSION_FRONT")
        SUSPENSION_FRONT,
        @SerialName("SUSPENSION_REAR")
        SUSPENSION_REAR,
        @SerialName("BRAKES")
        BRAKES,
        @SerialName("ELECTRONICS")
        ELECTRONICS,
        @SerialName("BODY")
        BODY,
        @SerialName("TRUNK")
        TRUNK,
        @SerialName("FUEL_SYSTEM")
        FUEL_SYSTEM,
        @SerialName("CABIN")
        CABIN,
        @SerialName("COUPLING")
        COUPLING,
        @SerialName("TRANSMISSION")
        TRANSMISSION
    }

    @Serializable
    enum class NodeStatus {
        @SerialName("NORMAL")
        NORMAL,
        @SerialName("NEEDS_CHECK")
        NEEDS_CHECK,
        @SerialName("BROKEN")
        BROKEN
    }
}