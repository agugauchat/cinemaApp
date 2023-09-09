package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.BookingRepository
import com.agugauchat.cinemaapp.domain.model.Booking
import javax.inject.Inject

class GetBookingsUseCase @Inject constructor(private val repository: BookingRepository) {
    suspend operator fun invoke(): List<Booking> {
        return repository.getBookings()
    }
}