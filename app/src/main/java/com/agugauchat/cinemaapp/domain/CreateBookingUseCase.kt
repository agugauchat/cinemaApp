package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.BookingRepository
import com.agugauchat.cinemaapp.data.database.entities.toDatabase
import com.agugauchat.cinemaapp.domain.model.Booking
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(private val repository: BookingRepository) {
    suspend operator fun invoke(booking: Booking) {
        return repository.insertBooking(booking.toDatabase())
    }
}