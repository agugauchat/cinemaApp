package com.agugauchat.cinemaapp.data

import com.agugauchat.cinemaapp.data.database.dao.BookingDao
import com.agugauchat.cinemaapp.data.database.entities.BookingEntity
import com.agugauchat.cinemaapp.domain.model.toDomain
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class BookingRepositoryTest {

    private lateinit var bookingRepository: BookingRepository
    private val bookingDao: BookingDao = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        bookingRepository = BookingRepository(bookingDao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getBookings should return list of bookings`() = runBlocking {
        // Given
        val bookingEntityList = listOf(
            BookingEntity(1, 12345, 1, "Movie 1", "10/10/2023", 1, 1000.0),
            BookingEntity(2, 12345, 1, "Movie 1", "10/10/2023", 1, 1000.0)
        )
        val bookingList = bookingEntityList.map { it.toDomain() }

        coEvery { bookingDao.getBookings() } returns bookingEntityList

        // When
        val result = bookingRepository.getBookings()

        // Then
        assertEquals(result.size, bookingList.size)

        coVerify(exactly = 1) { bookingDao.getBookings() }
    }

    @Test
    fun `insertBooking should insert booking entity`() = runBlocking {
        // Given
        val bookingEntity = BookingEntity(1, 12345, 1, "Movie 1", "10/10/2023", 1, 1000.0)

        coEvery { bookingDao.insertBooking(bookingEntity) } just Runs

        // When
        bookingRepository.insertBooking(bookingEntity)

        // Then
        coVerify(exactly = 1) { bookingDao.insertBooking(bookingEntity) }
    }

    @Test
    fun `removeBooking should remove booking by id`() = runBlocking {
        // Given
        val bookingId = "1"

        coEvery { bookingDao.removeBooking(bookingId) } returns 1

        // When
        bookingRepository.removeBooking(bookingId)

        // Then
        coVerify(exactly = 1) { bookingDao.removeBooking(bookingId) }
    }

    @Test
    fun `getOccupiedSeats should return occupied seats count`() = runBlocking {
        // Given
        val room = 2
        val date = "10/10/2023"
        val movie = "Movie 1"
        val occupiedSeats = 10

        coEvery { bookingDao.getOccupiedSeats(room, date, movie) } returns occupiedSeats

        // When
        val result = bookingRepository.getOccupiedSeats(room, date, movie)

        // Then
        assert(result == occupiedSeats)

        coVerify(exactly = 1) { bookingDao.getOccupiedSeats(room, date, movie) }
    }
}
