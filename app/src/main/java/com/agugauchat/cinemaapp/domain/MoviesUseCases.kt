package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.MovieRepository
import javax.inject.Inject

class MoviesUseCases @Inject constructor(private val repository: MovieRepository) {
    fun getMovies(): List<String> {
        return repository.getMovies()
    }
}