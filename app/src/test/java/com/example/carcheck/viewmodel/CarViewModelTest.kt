//package com.example.carcheck.viewmodel
//
//import com.example.carcheck.data.model.*
//import com.example.carcheck.data.repository.CarRepository
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.mockk
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.Test
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class CarViewModelTest {
//    private lateinit var viewModel: CarViewModel
//    private lateinit var repository: CarRepository
//
//    @BeforeEach
//    fun setUp() {
//        repository = mockk()
//        viewModel = CarViewModel(repository)
//    }
//
//    @Test
//    fun fetchCars() = runTest {
//        val testCars = listOf(
//            Car(1, "Toyota", "Camry", 2020),
//            Car(2, "Honda", "Civic", 2019)
//        )
//        coEvery { repository.getCars() } returns testCars
//
//        viewModel.fetchCars()
//        val result = viewModel.cars.value
//
//        Assertions.assertEquals(2, result?.size)
//        Assertions.assertEquals("Toyota", result?.get(0)?.brand ?: "")
//        coVerify { repository.getCars() }
//    }
//
//    @Test
//    fun fetchCarById() = runTest {
//        val testCar = Car(1, "Toyota", "Camry", 2020)
//        coEvery { repository.getCarById(1) } returns testCar
//
//        viewModel.fetchCarById(1)
//        val result = viewModel.selectedCar.value
//
//        Assertions.assertEquals(1, result?.carID)
//        Assertions.assertEquals("Toyota", result?.brand)
//        coVerify { repository.getCarById(1) }
//    }
//
//    @Test
//    fun fetchNodes() = runTest {
//        val testNodes = listOf(
//            Node(1, 1, "Engine", "10, 20"),
//            Node(2, 1, "Wheels", "30, 40")
//        )
//        coEvery { repository.getNodesByCar(1) } returns testNodes
//
//        viewModel.fetchNodes(1)
//        val result = viewModel.nodes.value
//
//        Assertions.assertEquals(2, result?.size)
//        Assertions.assertEquals("Engine", result?.get(0)?.name ?: "")
//        coVerify { repository.getNodesByCar(1) }
//    }
//
//    @Test
//    fun getParametersByNode() = runTest {
//        val testParams = listOf(
//            NodeParameters(1, 2, "Temperature", "90C"),
//            NodeParameters(2, 2, "Pressure", "2 bar")
//        )
//        coEvery { repository.getParametersByNode(2) } returns testParams
//
//        viewModel.fetchParametersByNode(2)
//        val result = viewModel.parameters.value
//
//        Assertions.assertEquals(2, result?.size)
//        Assertions.assertEquals("Temperature", result?.get(0)?.value ?: "")
//        coVerify { repository.getParametersByNode(2) }
//    }
//
//    @Test
//    fun getNodeChanges() = runTest {
//        val testChanges = listOf(
//            NodeChange(1, 3, "Changed oil", ""),
//            NodeChange(2, 3, "Replaced filter", "")
//        )
//        coEvery { repository.getNodeChanges(3) } returns testChanges
//
//        viewModel.fetchNodeChanges(3)
//        val result = viewModel.changes.value
//
//        Assertions.assertEquals(2, result?.size)
//        Assertions.assertEquals("Changed oil", result?.get(0)?.description ?: "")
//        coVerify { repository.getNodeChanges(3) }
//    }
//}
