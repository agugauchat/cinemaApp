package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.BookingRepository
import javax.inject.Inject

class DeleteBookingUseCase @Inject constructor(private val repository: BookingRepository) {
    suspend operator fun invoke(bookingId: Int) {
        repository.removeBooking(bookingId.toString())
    }
}