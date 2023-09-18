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

internal class BookingsUseCasesTest {

    @MockK
    private lateinit var bookingRepository: BookingRepository

    private lateinit var bookingsUseCases: BookingsUseCases

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        bookingsUseCases = BookingsUseCases(bookingRepository)
    }

    @Test
    fun `createBooking should insert booking into repository`() = runBlocking {
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
        bookingsUseCases.createBooking(booking)

        // Then
        coVerify(exactly = 1) { bookingRepository.insertBooking(booking.toDatabase()) }
    }

    @Test
    fun `deleteBooking should call removeBooking in repository with correct bookingId`() = runBlocking {
        // Given
        val bookingId = 123
        coEvery { bookingRepository.removeBooking(any()) } just runs

        // When
        bookingsUseCases.deleteBooking(bookingId)

        // Then
        coVerify(exactly = 1) { bookingRepository.removeBooking(bookingId.toString()) }
    }


    @Test
    fun `getBookings should return list of bookings and call repository once`() = runBlocking {
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
        val result = bookingsUseCases.getBookings()

        // Then
        coVerify(exactly = 1) { bookingRepository.getBookings() }
        assert(result == bookings)
    }


    @Test
    fun `getOccupiedSeats should return occupied seats count and call repository once`() = runBlocking {
        // Given
        val room = 1
        val date = "01/02/2023"
        val movie = "Movie 1"
        val occupiedSeats = 5

        coEvery { bookingRepository.getOccupiedSeats(room, date, movie) } returns occupiedSeats

        // When
        val result = bookingsUseCases.getOccupiedSeats(room, date, movie)

        // Then
        coVerify(exactly = 1) { bookingRepository.getOccupiedSeats(room, date, movie) }
        assert(result == occupiedSeats)
    }
}
