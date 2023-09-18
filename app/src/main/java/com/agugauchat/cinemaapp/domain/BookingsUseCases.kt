package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.BookingRepository
import com.agugauchat.cinemaapp.data.database.entities.toDatabase
import com.agugauchat.cinemaapp.domain.model.Booking
import javax.inject.Inject

class BookingsUseCases @Inject constructor(private val repository: BookingRepository) {

    suspend fun createBooking(booking: Booking) {
        return repository.insertBooking(booking.toDatabase())
    }

    suspend fun deleteBooking(bookingId: Int) {
        repository.removeBooking(bookingId.toString())
    }

    suspend fun getBookings(): List<Booking> {
        return repository.getBookings()
    }

    suspend fun getOccupiedSeats(room: Int, date: String, movie: String): Int? {
        return repository.getOccupiedSeats(room, date, movie)
    }
}