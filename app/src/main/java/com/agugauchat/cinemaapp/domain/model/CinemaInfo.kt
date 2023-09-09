package com.agugauchat.cinemaapp.domain.model

class CinemaInfo(
    val rooms_quantity: Int,
    val rooms_capacity: Int,
    val ticket_cost: Double,
    val rate_variations: List<RateVariation>
)
