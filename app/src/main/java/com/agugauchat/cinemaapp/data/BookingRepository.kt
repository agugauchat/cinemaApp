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

    // ToDo -> Implementar una función para validar si hay lugar en la sala
    // pasándole la capacidad máxima, la cantidad actual y los otros datos para hacer la query
    /*suspend fun checkFavorite(idNews: String): Int {
        return newsDAO.checkNews(idNews)
    }*/
}