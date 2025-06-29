package com.example.carcheck.data.model

import com.example.carcheck.data.model.CarNode.NodeCategory
import com.example.carcheck.data.model.CarNode.NodeStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NodePositions (
    @SerialName("id") val id: Int,
    @SerialName("car_id") val carId: Int,
    @SerialName("category") val category: NodeCategory,
    @SerialName("x_position") val x: Float,
    @SerialName("y_position") val y: Float
)