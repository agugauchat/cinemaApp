package com.agugauchat.cinemaapp.ui.bookings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.agugauchat.cinemaapp.domain.BookingsUseCases
import com.agugauchat.cinemaapp.domain.model.Booking
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class BookingsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var bookingsUseCases: BookingsUseCases

    private lateinit var viewModel: BookingsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)

        viewModel = BookingsViewModel(bookingsUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onCreate should set bookingList with result from getBookingsUseCase`() = runTest {
        // Given
        val bookings = listOf(
            Booking(
                id = 1,
                buyer_id = 1,
                cinema_room = 1,
                movie = "Movie 1",
                date = "01/02/2023",
                quantity = 2,
                total_price = 700.0
            ),
            Booking(
                id = 2,
                buyer_id = 2,
                cinema_room = 2,
                movie = "Movie 2",
                date = "01/02/2023",
                quantity = 4,
                total_price = 1400.0
            ),
        )
        coEvery { bookingsUseCases.getBookings() } returns bookings

        // When
        viewModel.onCreate()

        // Then
        assert(viewModel.bookingList.value == bookings)
    }

    @Test
    fun `deleteBooking should call deleteBookingUseCase and update bookingList`() = runTest {
        // Given
        val bookingId = 1
        coEvery { bookingsUseCases.deleteBooking(bookingId) } answers { /* Do nothing */ }
        val updatedBookings = listOf(
            Booking(
                id = 2,
                buyer_id = 2,
                cinema_room = 2,
                movie = "Movie 2",
                date = "01/02/2023",
                quantity = 4,
                total_price = 1400.0
            ),
        )
        coEvery { bookingsUseCases.getBookings() } returns updatedBookings

        // When
        viewModel.deleteBooking(bookingId)

        // Then
        coVerify(exactly = 1) { bookingsUseCases.deleteBooking(bookingId) }
        assert(viewModel.bookingList.value == updatedBookings)
    }
}
