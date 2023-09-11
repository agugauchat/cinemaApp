package com.agugauchat.cinemaapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agugauchat.cinemaapp.data.database.dao.BookingDao
import com.agugauchat.cinemaapp.data.database.entities.BookingEntity

@Database(entities = [BookingEntity::class], version = 1, exportSchema = false)
abstract class BookingDatabase : RoomDatabase() {

    abstract fun getBookingDao(): BookingDao
}