package com.agugauchat.cinemaapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agugauchat.cinemaapp.data.database.entities.BookingEntity

@Dao
interface BookingDao {
    @Query("SELECT * FROM booking_table")
    suspend fun getBookings():List<BookingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking:BookingEntity)

    @Query("DELETE FROM booking_table WHERE id = :id")
    suspend fun removeBooking(id: String): Int

    @Query("SELECT SUM(quantity) FROM booking_table WHERE cinema_room = :room AND date = :date AND movie = :movie")
    suspend fun getOccupiedSeats(room: Int, date: String, movie: String): Int?
}