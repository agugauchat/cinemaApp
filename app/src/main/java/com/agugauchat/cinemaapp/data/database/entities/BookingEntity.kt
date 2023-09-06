package com.agugauchat.cinemaapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "booking_table")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "buyer_id") val buyer_id: Int,
    @ColumnInfo(name = "cinema_room") val cinema_room: Int,
    @ColumnInfo(name = "movie") val movie: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "total_price") val total_price: Float
)