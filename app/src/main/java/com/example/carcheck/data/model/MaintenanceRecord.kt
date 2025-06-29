package com.example.carcheck.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MaintenanceRecord(
    @SerialName("id") val id: Int? = null,
    @SerialName("car_id") val carId: Int,
    @SerialName("maintenance_date") val date: String,
    @SerialName("description") val description: String?,
    @SerialName("next_due_date") val nextDueDate: String?,
)
