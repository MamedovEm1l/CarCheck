package com.example.carcheck.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NodeChange(
    @SerialName("id") val changeID: Int,
    @SerialName("node_id") val nodeID: Int,
    @SerialName("description") val description: String,
    @SerialName("change_date") val changeDate: String
)
