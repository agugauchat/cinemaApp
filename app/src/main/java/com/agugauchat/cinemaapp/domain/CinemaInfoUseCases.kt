package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.CinemaInfoRepository
import com.agugauchat.cinemaapp.domain.model.CinemaInfo
import javax.inject.Inject

class CinemaInfoUseCases @Inject constructor(private val repository: CinemaInfoRepository) {
    fun getCinemaInfo(): CinemaInfo {
        return repository.getCinemaInfo()
    }
}