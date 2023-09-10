package com.agugauchat.cinemaapp.data

import com.agugauchat.cinemaapp.domain.model.CinemaInfo
import com.agugauchat.cinemaapp.domain.model.RateVariation
import javax.inject.Inject

class CinemaInfoRepository @Inject constructor() {
    fun getCinemaInfo(): CinemaInfo {
        val rateVariations = listOf(
            RateVariation(1, 1.4),
            RateVariation(2, 0.7),
            RateVariation(3, 0.5),
            RateVariation(4, 0.7),
            RateVariation(5, 0.7),
            RateVariation(6, 1.4),
            RateVariation(7, 1.4)
        )
        return CinemaInfo(
            rooms_quantity = 4,
            rooms_capacity = 60,
            ticket_cost = 1000.0,
            rateVariations
        )
    }
}
