package com.agugauchat.cinemaapp.data

import com.agugauchat.cinemaapp.data.database.dao.BookingDao
import com.agugauchat.cinemaapp.data.database.entities.BookingEntity
import com.agugauchat.cinemaapp.domain.model.Booking
import com.agugauchat.cinemaapp.domain.model.toDomain
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val bookingDao: BookingDao
) {
    suspend fun getBookings(): List<Booking> {
        val response: List<BookingEntity> = bookingDao.getBookings()
        return response.map { it.toDomain() }
    }

    suspend fun insertBooking(booking: BookingEntity) {
        bookingDao.insertBooking(booking)
    }

    suspend fun removeBooking(id: String) {
        bookingDao.removeBooking(id)
    }

    suspend fun getOccupiedSeats(room: Int, date: String, movie: String): Int? {
        return bookingDao.getOccupiedSeats(room, date, movie)
    }
}