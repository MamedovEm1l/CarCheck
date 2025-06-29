package com.example.carcheck.data.repository

import com.example.carcheck.data.model.Cars
import com.example.carcheck.data.model.CarNode
import com.example.carcheck.data.model.MaintenanceNodeTask
import com.example.carcheck.data.model.MaintenanceRecord
import com.example.carcheck.data.model.NodeChange
import com.example.carcheck.data.model.NodeParameters
import com.example.carcheck.data.model.NodePositions

interface CarRepository {
    suspend fun getCars(): List<Cars>?
    suspend fun getCarById(id: Int): Cars?
    suspend fun getNodePositions(carID: Int): List<NodePositions>
    suspend fun getCarNodes(carID: Int): List<CarNode>
    suspend fun getParametersByNode(nodeID: Int): List<NodeParameters>
    suspend fun getNodeChanges(nodeID: Int): List<NodeChange>
    suspend fun updateNodeStatus(nodeID: Int, newStatus: String)
    suspend fun getMaintenanceRecords(carId: Int): List<MaintenanceRecord>
    suspend fun getNodeTasksForRecord(recordId: Int?): List<MaintenanceNodeTask>
    suspend fun addMaintenanceRecord(record: MaintenanceRecord): MaintenanceRecord
    suspend fun addNodeTask(task: MaintenanceNodeTask)
    suspend fun getNodeTasksByMaintenanceId(maintenanceId: Int): List<MaintenanceNodeTask>
    suspend fun getNodesByCategory(carId: Int, category: String): List<CarNode>
}

