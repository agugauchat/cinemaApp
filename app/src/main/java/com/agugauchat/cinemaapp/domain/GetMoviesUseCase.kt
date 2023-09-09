package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.MovieRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke():List<String>{
        return repository.getMovies()
    }
}