package com.agugauchat.cinemaapp.domain

import com.agugauchat.cinemaapp.data.CinemaInfoRepository
import com.agugauchat.cinemaapp.domain.model.CinemaInfo
import javax.inject.Inject

class GetCinemaInfoUseCase @Inject constructor(private val repository: CinemaInfoRepository) {
    operator fun invoke(): CinemaInfo {
        return repository.getCinemaInfo()
    }
}