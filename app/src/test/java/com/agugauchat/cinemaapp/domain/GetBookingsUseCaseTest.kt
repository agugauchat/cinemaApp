package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.BookingRepository
import com.agugauchat.cinemaapp.domain.model.Booking
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class GetBookingsUseCaseTest {

    @MockK
    private lateinit var bookingRepository: BookingRepository

    lateinit var getBookingsUseCase: GetBookingsUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getBookingsUseCase = GetBookingsUseCase(bookingRepository)
    }

    @Test
    fun `invoke should return list of bookings and call repository once`() = runBlocking {
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
        coEvery { bookingRepository.getBookings() } returns bookings

        // When
        val result = getBookingsUseCase()

        // Then
        coVerify(exactly = 1) { bookingRepository.getBookings() }
        assert(result == bookings)
    }
}