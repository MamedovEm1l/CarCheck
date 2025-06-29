package com.example.carcheck.data.repository

import com.example.carcheck.data.model.Cars
import com.example.carcheck.data.model.CarNode
import com.example.carcheck.data.model.MaintenanceNodeTask
import com.example.carcheck.data.model.MaintenanceRecord
import com.example.carcheck.data.model.NodeChange
import com.example.carcheck.data.model.NodeParameters
import com.example.carcheck.data.model.NodePositions
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : CarRepository {
    override suspend fun getCars(): List<Cars> {
        return withContext(Dispatchers.IO) {
            postgrest.from("cars").select().decodeList<Cars>()
        }
    }

    override suspend fun getCarById(id: Int): Cars? {
        return withContext(Dispatchers.IO) {
            postgrest.from("cars").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingleOrNull<Cars>()
        }
    }

    override suspend fun getNodePositions(carID: Int): List<NodePositions> {
        return withContext(Dispatchers.IO) {
            postgrest.from("node_positions").select {
                filter {
                    eq("car_id", carID)
                }
            }.decodeList<NodePositions>()
        }
    }

    override suspend fun getCarNodes(carID: Int): List<CarNode> {
        return postgrest.from("car_nodes")
            .select{
                filter {
                    eq("car_id", carID)
                }
            }
            .decodeList<CarNode>()
    }

    override suspend fun getParametersByNode(nodeID: Int): List<NodeParameters> {
        return postgrest.from("node_parameters")
            .select {
                filter {
                    eq("node_id", nodeID)
                }
            }
            .decodeList<NodeParameters>()
    }

    override suspend fun getNodeChanges(nodeID: Int): List<NodeChange> {
        return postgrest.from("node_changes")
            .select {
                filter {
                    eq("node_id", nodeID)
                }
            }
            .decodeList<NodeChange>()
    }

    override suspend fun updateNodeStatus(nodeID: Int, newStatus: String) {
        withContext(Dispatchers.IO) {
            postgrest.from("car_nodes").update(
                mapOf("status" to newStatus)
            ) {
                filter {
                    eq("id", nodeID)
                }
            }
        }
    }

    override suspend fun getMaintenanceRecords(carId: Int): List<MaintenanceRecord> {
        return withContext(Dispatchers.IO) {
            postgrest.from("maintenance_records").select {
                filter {
                    eq("car_id", carId)
                }
            }.decodeList<MaintenanceRecord>()
        }
    }

    override suspend fun getNodeTasksForRecord(recordId: Int?): List<MaintenanceNodeTask> {
        return withContext(Dispatchers.IO) {
            postgrest.from("maintenance_node_tasks").select {
                filter {
                    eq("maintenance_id", recordId!!.toInt())
                }
            }.decodeList<MaintenanceNodeTask>()
        }
    }

    override suspend fun addMaintenanceRecord(record: MaintenanceRecord): MaintenanceRecord {
        return withContext(Dispatchers.IO) {
            postgrest.from("maintenance_records")
                .insert(record) {
                    select()
                }
                .decodeSingle()
        }
    }

    override suspend fun addNodeTask(task: MaintenanceNodeTask) {
        withContext(Dispatchers.IO) {
            postgrest.from("maintenance_node_tasks").insert(task)
        }
    }

    override suspend fun getNodeTasksByMaintenanceId(maintenanceId: Int): List<MaintenanceNodeTask> {
        return withContext(Dispatchers.IO) {
            postgrest.from("maintenance_node_tasks")
                .select {
                    filter {
                        eq("maintenance_id", maintenanceId)
                    }
                }.decodeList()
        }
    }

    override suspend fun getNodesByCategory(carId: Int, category: String): List<CarNode> {
        return withContext(Dispatchers.IO) {
            postgrest.from("car_nodes").select {
                filter {
                    eq("car_id", carId)
                    eq("category", category)
                }
            }.decodeList<CarNode>()
        }
    }

}