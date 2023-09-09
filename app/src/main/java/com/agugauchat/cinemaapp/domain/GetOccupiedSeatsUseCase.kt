package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.BookingRepository
import javax.inject.Inject

class GetOccupiedSeatsUseCase @Inject constructor(private val bookingRepository: BookingRepository) {
    suspend operator fun invoke(room: Int, date: String, movie: String): Int? {
        return bookingRepository.getOccupiedSeats(room, date, movie)
    }
}