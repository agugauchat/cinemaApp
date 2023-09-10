package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.MovieRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

internal class GetMoviesUseCaseTest {

    @MockK
    private lateinit var movieRepository: MovieRepository

    lateinit var getMoviesUseCase: GetMoviesUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getMoviesUseCase = GetMoviesUseCase(movieRepository)
    }

    @Test
    fun `invoke should return movies and call repository once`() {
        // Given
        val movies = listOf("Avatar", "Back to the Future", "Iron Man", "Scarface", "Titanic")
        coEvery { movieRepository.getMovies() } returns movies

        // When
        val result = getMoviesUseCase()

        // Then
        coVerify(exactly = 1) { movieRepository.getMovies() }
        assert(result == movies)
    }
}
