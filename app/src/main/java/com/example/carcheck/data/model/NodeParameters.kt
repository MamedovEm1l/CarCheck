package com.example.carcheck.data.model

import com.example.carcheck.data.model.CarNode.NodeStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NodeParameters(
    @SerialName("id") val id: Int,
    @SerialName("node_id") val nodeID: Int,
    @SerialName("name") val name: String,
    @SerialName("value") val value: String,
    @SerialName("status") val status: NodeStatus
)