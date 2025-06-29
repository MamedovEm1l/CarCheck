package com.example.carcheck.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carcheck.data.model.CarNode
import com.example.carcheck.data.model.CarNode.NodeStatus
import com.example.carcheck.data.model.Cars
import com.example.carcheck.data.model.MaintenanceNodeTask
import com.example.carcheck.data.model.MaintenanceRecord
import com.example.carcheck.data.model.NodeChange
import com.example.carcheck.data.model.NodeParameters
import com.example.carcheck.data.model.NodePositions
import com.example.carcheck.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    private val _cars = MutableStateFlow<List<Cars>?>(null)
    val cars: StateFlow<List<Cars>?> get() = _cars

    private val _positions = MutableStateFlow<List<NodePositions>?>(null)
    val positions: StateFlow<List<NodePositions>?> get() = _positions

    private val _nodes = MutableStateFlow<List<CarNode>?>(null)
    val nodes: StateFlow<List<CarNode>?> get() = _nodes

    private val _selectedCar = MutableStateFlow<Cars?>(null)
    val selectedCar: StateFlow<Cars?> get() = _selectedCar

    private val _parameters = MutableStateFlow<List<NodeParameters>?>(null)
    val parameters: StateFlow<List<NodeParameters>?> get() = _parameters

    private val _changes = MutableStateFlow<List<NodeChange>?>(null)
    val changes: StateFlow<List<NodeChange>?> get() = _changes

    private val _maintenanceRecords = MutableStateFlow<List<MaintenanceRecord>?>(null)
    val maintenanceRecords: StateFlow<List<MaintenanceRecord>?> get() = _maintenanceRecords

    private val _nodeTasks = MutableStateFlow<List<MaintenanceNodeTask>?>(null)
    val nodeTasks: StateFlow<List<MaintenanceNodeTask>?> get() = _nodeTasks

    private val _selectedNode = MutableStateFlow<CarNode?>(null)
    val selectedNode: StateFlow<CarNode?> get() = _selectedNode

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchCars() {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val result = repository.getCars() // или твоя логика
                _cars.value = result
            } catch (e: Exception) {
                _cars.value = emptyList()
                _errorMessage.value = when (e) {
                    is IOException -> "Нет подключения к интернету"
                    else -> "Произошла ошибка: ${e.localizedMessage}"
                }
            }
        }
    }
    fun loadCategoryDataForNode(node: CarNode) {
        viewModelScope.launch {
            val allNodesInCategory = repository.getNodesByCategory(node.carId, node.category.name)

            val paramsDeferred = allNodesInCategory.map { n ->
                async { repository.getParametersByNode(n.id) }
            }
            val changesDeferred = allNodesInCategory.map { n ->
                async { repository.getNodeChanges(n.id) }
            }

            val allParams = paramsDeferred.awaitAll().flatten()
            val allChanges = changesDeferred.awaitAll().flatten()

            _parameters.value = allParams
            _changes.value = allChanges
        }
    }


    private val criticalParams =
        setOf("Температура масла", "Давление масла", "Температура охлаждающей жидкости")

    init {
        simulateParameterChanges()
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun simulateParameterChanges() {
        viewModelScope.launch {
            while (true) {
                delay(4000L)
                val updatedParams = _parameters.value?.map { param ->
                    if (param.name in criticalParams) {
                        val newValue = simulateNewValue(param)
                        val newStatus = if (shouldBeCritical(
                                param.name,
                                newValue
                            )
                        ) NodeStatus.BROKEN else NodeStatus.NORMAL
                        param.copy(value = newValue, status = newStatus)
                    } else param
                }
                _parameters.value = updatedParams
            }
        }
    }

    private fun simulateNewValue(param: NodeParameters): String {
        return when (param.name) {
            "Температура масла" -> (90 + (0..10).random()).toString()
            "Давление масла" -> "%.1f".format(2.0 + (0..20).random() / 10.0)
            "Температура охлаждающей жидкости" -> (80 + (0..20).random()).toString()
            else -> param.value
        }
    }

    private fun shouldBeCritical(name: String, value: String): Boolean {
        val value = value.replace(',', '.')
        return when (name) {
            "Температура масла" -> value.toFloat() > 100
            "Давление масла" -> value.toFloat() < 2.2
            "Температура охлаждающей жидкости" -> value.toFloat() > 100
            else -> false
        }
    }

    fun selectCar(car: Cars) {
        _selectedCar.value = car
        fetchNodePositions(car.id)
        fetchNodes(car.id)
        fetchMaintenanceRecords(car.id)
        fetchCarById(car.id)
        Log.i("car", car.id.toString())
    }

    fun clearSelectedNode() {
        _selectedNode.value = null
    }

    fun selectNode(node: CarNode) {
        Log.i("viewmod", "yess")
        _selectedNode.value = node
        fetchParametersByNode(node.id)
        fetchNodeChanges(node.id)
    }

    fun fetchAllNodeTasksForCar(carId: Int) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val records = repository.getMaintenanceRecords(carId)
                val allTasks = mutableListOf<MaintenanceNodeTask>()
                for (record in records) {
                    val tasks = repository.getNodeTasksByMaintenanceId(record.id!!)
                    allTasks.addAll(tasks)
                }
                _nodeTasks.value = allTasks
            } catch (e: Exception) {
                _nodeTasks.value = emptyList()
                _errorMessage.value = when (e) {
                    is IOException -> "Нет подключения к интернету"
                    else -> "Ошибка загрузки задач узлов: ${e.localizedMessage}"
                }
            }
        }
    }

    fun fetchCarById(id: Int) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                _selectedCar.value = repository.getCarById(id)
            } catch (e: Exception) {
                _selectedCar.value = null
                _errorMessage.value = "Ошибка загрузки машины: ${e.localizedMessage}"
            }
        }
    }

    fun fetchNodePositions(carID: Int) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                _positions.value = repository.getNodePositions(carID)
            } catch (e: Exception) {
                _positions.value = emptyList()
                _errorMessage.value = "Ошибка загрузки позиций узлов: ${e.localizedMessage}"
            }
        }
    }

    fun fetchNodes(carID: Int) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val fetchedNodes = repository.getCarNodes(carID)
                Log.d("CarViewModel", "Загруженные узлы: $fetchedNodes")
                _nodes.value = fetchedNodes
            } catch (e: Exception) {
                _nodes.value = emptyList()
                _errorMessage.value = "Ошибка загрузки узлов: ${e.localizedMessage}"
            }
        }
    }

    fun fetchParametersByNode(nodeId: Int) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val fetchedParameters = repository.getParametersByNode(nodeId)
                Log.d("CarViewModel", "Загруженные параметры: $fetchedParameters")
                _parameters.value = fetchedParameters
            } catch (e: Exception) {
                _parameters.value = emptyList()
                _errorMessage.value = "Ошибка загрузки параметров узла: ${e.localizedMessage}"
            }
        }
    }

    fun fetchNodeChanges(nodeId: Int) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                _changes.value = repository.getNodeChanges(nodeId)
            } catch (e: Exception) {
                _changes.value = emptyList()
                _errorMessage.value = "Ошибка загрузки истории изменений: ${e.localizedMessage}"
            }
        }
    }



    private var allMaintenanceRecords: List<MaintenanceRecord> = emptyList()
    fun filterMaintenanceRecordsByDateRange(start: LocalDate?, end: LocalDate?) {
        if (start == null || end == null) return

        val filtered = allMaintenanceRecords.filter { record ->
            val recordDate = record.date.toLocalDate()
            Log.i("data1","Record date: ${record.date}, Parsed: $recordDate") // <--- отладка
            recordDate != null && recordDate in start..end
        }

        Log.i("data","Filtered count: ${filtered.size}") // <--- отладка

        _maintenanceRecords.value = filtered
    }


    /*fun filterMaintenanceRecordsByDateRange(from: LocalDate?, to: LocalDate?) {
        if (from == null || to == null) {
            _maintenanceRecords.value = allMaintenanceRecords
            return
        }

        val filtered = allMaintenanceRecords.filter { record ->
            val recordDate = try {
                LocalDateTime.parse(record.date, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate()
            } catch (e: Exception) {
                null
            }

            recordDate != null && !recordDate.isBefore(from) && !recordDate.isAfter(to)
        }

        _maintenanceRecords.value = filtered
    }*/


    fun fetchMaintenanceRecords(carId: Int) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val records = repository.getMaintenanceRecords(carId)
                allMaintenanceRecords = records // <--- ВАЖНО!
                _maintenanceRecords.value = records
            } catch (e: Exception) {
                allMaintenanceRecords = emptyList() // <--- на всякий случай
                _maintenanceRecords.value = emptyList()
                _errorMessage.value = "Ошибка загрузки записей ТО: ${e.localizedMessage}"
            }
        }
    }


    fun fetchNodeTasksForRecord(recordId: Int?) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                _nodeTasks.value = repository.getNodeTasksForRecord(recordId)
            } catch (e: Exception) {
                _nodeTasks.value = emptyList()
                _errorMessage.value = "Ошибка загрузки задач ТО: ${e.localizedMessage}"
            }
        }
    }

    fun addMaintenanceRecord(
        record: MaintenanceRecord,
        onComplete: (MaintenanceRecord) -> Unit = {}
    ) {
        Log.d("CarVie", "Добавление записи ТО: $record")
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val newRecord = repository.addMaintenanceRecord(record)
                fetchMaintenanceRecords(newRecord.carId)
                onComplete(newRecord)
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка добавления записи ТО: ${e.localizedMessage}"
            }
        }
    }

    fun addNodeTask(task: MaintenanceNodeTask, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                repository.addNodeTask(task)
                fetchNodeTasksForRecord(task.maintenanceId)
                onComplete()
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка добавления задачи узла: ${e.localizedMessage}"
            }
        }
    }

}

fun String.toLocalDate(): LocalDate? {
    return try {
        when {
            this.length == 10 -> LocalDate.parse(this) // "2025-05-15"
            this.length > 10 -> LocalDateTime.parse(this).toLocalDate() // "2025-05-15T00:00:00"
            else -> null
        }
    } catch (e: DateTimeParseException) {
        e.printStackTrace()
        null
    }
}