package com.agugauchat.cinemaapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agugauchat.cinemaapp.domain.CreateBookingUseCase
import com.agugauchat.cinemaapp.domain.GetCinemaInfoUseCase
import com.agugauchat.cinemaapp.domain.GetMoviesUseCase
import com.agugauchat.cinemaapp.domain.GetOccupiedSeatsUseCase
import com.agugauchat.cinemaapp.domain.model.CinemaInfo
import com.agugauchat.cinemaapp.domain.model.RateVariation
import com.agugauchat.cinemaapp.ui.utils.UtilsUi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlin.math.ceil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class BuyTicketsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var getMoviesUseCase: GetMoviesUseCase

    @RelaxedMockK
    private lateinit var getCinemaInfoUseCase: GetCinemaInfoUseCase

    @MockK
    private lateinit var createBookingUseCase: CreateBookingUseCase

    @MockK
    private lateinit var getOccupiedSeatsUseCase: GetOccupiedSeatsUseCase

    private lateinit var viewModel: BuyTicketsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)

        viewModel = BuyTicketsViewModel(
            getMoviesUseCase, getCinemaInfoUseCase, createBookingUseCase, getOccupiedSeatsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onCreate should set movieList and cinemaInfo LiveData`() = runTest {
        // Given
        val movies = listOf("Movie 1", "Movie 2")
        val cinemaInfo = CinemaInfo(
            rooms_quantity = 4,
            rooms_capacity = 60,
            ticket_cost = 1000.0,
            rate_variations = emptyList()
        )
        coEvery { getMoviesUseCase() } returns movies
        coEvery { getCinemaInfoUseCase() } returns cinemaInfo

        // When
        viewModel = BuyTicketsViewModel(
            getMoviesUseCase, getCinemaInfoUseCase, createBookingUseCase, getOccupiedSeatsUseCase
        )

        // Then
        assert(viewModel.movieList.value == movies)
        assert(viewModel.cinemaInfo.value == cinemaInfo)
    }

    @Test
    fun `recalculatePrice should calculate total price correctly`() {
        // Given
        val dayOfWeek = 1
        val ticketBaseCost = 1000.0
        val pricePercentage = 1.5
        val expectedTotalPrice = ceil(3 * 1000.0 * 1.5) // 4500.0
        viewModel.date.value = "10/09/2023" // Sunday = 1
        viewModel.quantity.value = "3"
        viewModel.cinemaInfo.value = CinemaInfo(
            rooms_quantity = 4,
            rooms_capacity = 60,
            ticket_cost = ticketBaseCost,
            rate_variations = listOf(
                RateVariation(dayOfWeek, pricePercentage)
            )
        )

        // When
        viewModel.recalculatePrice()

        // Then
        assert(viewModel.totalPrice.value == expectedTotalPrice)
    }


    @Test
    fun `recalculatePrice should return zero when ticketBaseCost is zero`() {
        // Given
        val dayOfWeek = 1
        val ticketBaseCost = 0.0
        val pricePercentage = 1.5
        val expectedTotalPrice = 0.0
        viewModel.date.value = "10/09/2023" // Sunday = 1
        viewModel.quantity.value = "2"
        viewModel.cinemaInfo.value = CinemaInfo(
            rooms_quantity = 4,
            rooms_capacity = 60,
            ticket_cost = ticketBaseCost,
            rate_variations = listOf(
                RateVariation(dayOfWeek, pricePercentage)
            )
        )

        // When
        viewModel.recalculatePrice()

        // Then
        assert(viewModel.totalPrice.value == expectedTotalPrice)
    }

    @Test
    fun `buyTickets should create booking successfully`() = runBlocking {
        // Given
        val movieTitle = "Movie 1"
        val roomName = "Sala 2"
        val bookingDate = "10/09/2023"
        val bookingQuantity = "3"
        val buyerIdentifier = "12345"
        val totalPrice = 4500.0
        val cinemaRoomNumber = 2
        val occupiedSeats = 30

        val cinemaInfo = CinemaInfo(
            rooms_quantity = 4,
            rooms_capacity = 60,
            ticket_cost = 1000.0,
            rate_variations = emptyList()
        )
        viewModel.cinemaInfo.value = cinemaInfo

        viewModel.totalPrice.value = totalPrice

        coEvery {
            getOccupiedSeatsUseCase(
                cinemaRoomNumber,
                bookingDate,
                movieTitle
            )
        } returns occupiedSeats
        coEvery { createBookingUseCase(any()) } coAnswers { }

        // When
        viewModel.buyTickets(movieTitle, roomName, bookingDate, bookingQuantity, buyerIdentifier)

        // Then
        coVerify(exactly = 1) { createBookingUseCase(any()) }
        assert(viewModel.statusEvent.value == UtilsUi.STATUS_SUCCESS)
    }

    @Test
    fun `buyTickets should handle incomplete data`() = runTest {
        // When
        viewModel.buyTickets("", "", "", "", "")

        // Then
        assert(viewModel.statusEvent.value == UtilsUi.STATUS_INCOMPLETE_DATA)
    }

    @Test
    fun `buyTickets should handle conversion error`() = runTest {
        // When
        viewModel.totalPrice.value = 4500.0
        viewModel.buyTickets("Movie 1", "Sala 2", "10/10/2023", "3", "invalid_buyer_id")

        // Then
        assert(viewModel.statusEvent.value == UtilsUi.STATUS_CONVERSION_ERROR)
    }

    @Test
    fun `buyTickets should handle no seats available`() = runTest {
        // Given
        val cinemaRoom = "Sala 2"
        val cinemaRoomNumber = 2
        val date = "10/10/2023"
        val movie = "Movie 1"
        val quantity = "5"
        val buyerId = "12345"
        val maximumCapacity = 60
        val occupiedSeats = 58
        viewModel.totalPrice.value = 4500.0
        coEvery { getOccupiedSeatsUseCase(cinemaRoomNumber, date, movie) } returns occupiedSeats
        val cinemaInfo = CinemaInfo(
            rooms_quantity = 4,
            rooms_capacity = maximumCapacity,
            ticket_cost = 1000.0,
            rate_variations = emptyList()
        )
        viewModel.cinemaInfo.value = cinemaInfo

        // When
        viewModel.buyTickets(movie, cinemaRoom, date, quantity, buyerId)

        // Then
        assert(viewModel.statusEvent.value == UtilsUi.STATUS_NO_SEATS)
    }

    @Test
    fun `clearStatusEvent should reset statusEvent LiveData`() {
        // Given
        viewModel.statusEvent.value = UtilsUi.STATUS_SUCCESS

        // When
        viewModel.clearStatusEvent()

        // Then
        assert(viewModel.statusEvent.value == null)
    }

    @Test
    fun `extractCinemaRoomNumber should extract cinema room number from room string`() {
        // When
        val room = "Sala 2"
        val expectedRoomNumber = 2

        // Then
        assert(viewModel.extractCinemaRoomNumber(room) == expectedRoomNumber)
    }

    @Test
    fun `extractCinemaRoomNumber should return null when room string doesn't contain a number`() {
        // When
        val room = "Invalid value"

        // Then
        assert(viewModel.extractCinemaRoomNumber(room) == null)
    }
}
