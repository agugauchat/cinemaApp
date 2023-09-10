package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.BookingRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class GetOccupiedSeatsUseCaseTest {

    @MockK
    private lateinit var bookingRepository: BookingRepository

    private lateinit var getOccupiedSeatsUseCase: GetOccupiedSeatsUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getOccupiedSeatsUseCase = GetOccupiedSeatsUseCase(bookingRepository)
    }

    @Test
    fun `invoke should return occupied seats count and call repository once`() = runBlocking {
        // Given
        val room = 1
        val date = "01/02/2023"
        val movie = "Movie 1"
        val occupiedSeats = 5

        coEvery { bookingRepository.getOccupiedSeats(room, date, movie) } returns occupiedSeats

        // When
        val result = getOccupiedSeatsUseCase(room, date, movie)

        // Then
        coVerify(exactly = 1) { bookingRepository.getOccupiedSeats(room, date, movie) }
        assert(result == occupiedSeats)
    }
}
