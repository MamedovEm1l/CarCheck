package com.example.carcheck.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MaintenanceNodeTask(
    @SerialName("id") val id: Int? = null,
    @SerialName("maintenance_id") val maintenanceId: Int?, // изменено с maintenance_record_id
    @SerialName("task_description") val taskDescription: String, // исправлено имя поля
    @SerialName("category") val category: String?, // добавлено новое поле
)
