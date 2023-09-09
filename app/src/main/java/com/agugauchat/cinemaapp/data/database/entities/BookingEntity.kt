package com.agugauchat.cinemaapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.agugauchat.cinemaapp.domain.model.Booking

@Entity(tableName = "booking_table")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "buyer_id") val buyer_id: Int,
    @ColumnInfo(name = "cinema_room") val cinema_room: Int,
    @ColumnInfo(name = "movie") val movie: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "total_price") val total_price: Double
)

fun Booking.toDatabase() = BookingEntity(
    buyer_id = buyer_id,
    cinema_room = cinema_room,
    movie = movie,
    date = date,
    quantity = quantity,
    total_price = total_price
)