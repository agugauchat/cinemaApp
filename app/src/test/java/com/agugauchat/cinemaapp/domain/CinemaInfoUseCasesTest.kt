package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.CinemaInfoRepository
import com.agugauchat.cinemaapp.domain.model.CinemaInfo
import com.agugauchat.cinemaapp.domain.model.RateVariation
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

internal class CinemaInfoUseCasesTest {

    @MockK
    private lateinit var cinemaInfoRepository: CinemaInfoRepository

    lateinit var cinemaInfoUseCases: CinemaInfoUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        cinemaInfoUseCases = CinemaInfoUseCases(cinemaInfoRepository)
    }

    @Test
    fun `invoke should return cinema info and call repository once`() {
        // Given
        val cinemaInfo = CinemaInfo(
            rooms_quantity = 4,
            rooms_capacity = 60,
            ticket_cost = 1000.0,
            rate_variations = listOf(
                RateVariation(1, 1.4),
                RateVariation(2, 0.7)
            )
        )
        coEvery { cinemaInfoRepository.getCinemaInfo() } returns cinemaInfo

        // When
        val result = cinemaInfoUseCases.getCinemaInfo()

        // Then
        coVerify(exactly = 1) { cinemaInfoRepository.getCinemaInfo() }
        assert(result == cinemaInfo)
    }
}
