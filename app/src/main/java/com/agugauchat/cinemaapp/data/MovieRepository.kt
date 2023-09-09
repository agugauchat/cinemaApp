package com.agugauchat.cinemaapp.data

import javax.inject.Inject

class MovieRepository @Inject constructor() {
    fun getMovies(): List<String> {
        return listOf("Avatar","Back to the Future", "Iron Man", "Scarface", "Titanic")
    }
}