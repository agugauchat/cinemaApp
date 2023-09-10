package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.BookingRepository
import com.agugauchat.cinemaapp.data.database.entities.toDatabase
import com.agugauchat.cinemaapp.domain.model.Booking
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class CreateBookingUseCaseTest {

    @MockK
    private lateinit var bookingRepository: BookingRepository

    private lateinit var createBookingUseCase: CreateBookingUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        createBookingUseCase = CreateBookingUseCase(bookingRepository)
    }

    @Test
    fun `invoke should insert booking into repository`() = runBlocking {
        // Given
        val booking = Booking(
            buyer_id = 1,
            cinema_room = 1,
            movie = "Movie 1",
            date = "01/02/2023",
            quantity = 2,
            total_price = 1400.0
        )
        coEvery { bookingRepository.insertBooking(any()) } just runs

        // When
        createBookingUseCase(booking)

        // Then
        coVerify(exactly = 1) { bookingRepository.insertBooking(booking.toDatabase()) }
    }
}
