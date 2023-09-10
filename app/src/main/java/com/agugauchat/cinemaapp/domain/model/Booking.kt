package com.agugauchat.cinemaapp.domain.model

import com.agugauchat.cinemaapp.data.database.entities.BookingEntity

class Booking(
    val id: Int = 0,
    val buyer_id: Int,
    val cinema_room: Int,
    val movie: String,
    val date: String,
    val quantity: Int,
    val total_price: Double
)

fun BookingEntity.toDomain() =
    Booking(id, buyer_id, cinema_room, movie, date, quantity, total_price)