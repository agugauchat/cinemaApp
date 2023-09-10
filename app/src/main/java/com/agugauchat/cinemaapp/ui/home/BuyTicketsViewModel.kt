package com.agugauchat.cinemaapp.ui.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agugauchat.cinemaapp.domain.CreateBookingUseCase
import com.agugauchat.cinemaapp.domain.GetCinemaInfoUseCase
import com.agugauchat.cinemaapp.domain.GetMoviesUseCase
import com.agugauchat.cinemaapp.domain.GetOccupiedSeatsUseCase
import com.agugauchat.cinemaapp.domain.model.Booking
import com.agugauchat.cinemaapp.domain.model.CinemaInfo
import com.agugauchat.cinemaapp.ui.utils.UtilsUi.STATUS_CONVERSION_ERROR
import com.agugauchat.cinemaapp.ui.utils.UtilsUi.STATUS_INCOMPLETE_DATA
import com.agugauchat.cinemaapp.ui.utils.UtilsUi.STATUS_NO_SEATS
import com.agugauchat.cinemaapp.ui.utils.UtilsUi.STATUS_SUCCESS
import com.agugauchat.cinemaapp.ui.utils.allNotNull
import com.agugauchat.cinemaapp.ui.utils.toDayOfWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class BuyTicketsViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getCinemaInfoUseCase: GetCinemaInfoUseCase,
    private val createBookingUseCase: CreateBookingUseCase,
    private val getOccupiedSeatsUseCase: GetOccupiedSeatsUseCase
) : ViewModel() {

    val movieList = MutableLiveData<List<String>>()
    val cinemaInfo = MutableLiveData<CinemaInfo>()
    val date = MutableLiveData<String>()
    val quantity = MutableLiveData<String>()
    val totalPrice = MediatorLiveData<Double>()
    val statusEvent = MutableLiveData<Int?>()
    val cleanFieldsEvent = MutableLiveData<Unit>()

    init {
        viewModelScope.launch {
            val result = getMoviesUseCase()
            movieList.postValue(result)
        }
        viewModelScope.launch {
            val result = getCinemaInfoUseCase()
            cinemaInfo.postValue(result)
        }
        totalPrice.addSource(date) { recalculatePrice() }
        totalPrice.addSource(quantity) { recalculatePrice() }
    }

    private fun recalculatePrice() {
        val dayOfWeek = date.value.toDayOfWeek()

        val ticketsQuantity = quantity.value?.toIntOrNull() ?: 1
        val ticketBaseCost = cinemaInfo.value?.ticket_cost ?: 0.0
        val pricePercentage =
            cinemaInfo.value?.rate_variations?.find { it.day_of_week == dayOfWeek }?.price_percentage
                ?: 1.0

        if (dayOfWeek != null && ticketsQuantity > 0 && ticketBaseCost > 0) {
            totalPrice.value = ceil(ticketsQuantity * ticketBaseCost * pricePercentage)
        } else {
            totalPrice.value = 0.0
        }
    }

    fun buyTickets(movie: String, room: String, date: String, quantity: String, buyerId: String) {
        val totalPrice = totalPrice.value

        if (!allNotNull(movie, room, date, quantity, buyerId, totalPrice)) {
            statusEvent.postValue(STATUS_INCOMPLETE_DATA)
            return
        }

        val cinemaRoomNumber = extractCinemaRoomNumber(room)
        val quantityValue = quantity.toIntOrNull()
        val buyerIdValue = buyerId.toIntOrNull()
        val maximumCapacity = cinemaInfo.value?.rooms_capacity ?: 0

        if (cinemaRoomNumber == null || quantityValue == null || buyerIdValue == null) {
            statusEvent.postValue(STATUS_CONVERSION_ERROR)
            return
        }

        viewModelScope.launch {
            val occupiedSeats = getOccupiedSeatsUseCase(cinemaRoomNumber, date, movie) ?: 0

            val hasAvailableSeats = maximumCapacity - occupiedSeats >= quantityValue
            if (hasAvailableSeats) {
                viewModelScope.launch {
                    createBookingUseCase(
                        Booking(
                            movie = movie,
                            cinema_room = cinemaRoomNumber,
                            date = date,
                            quantity = quantityValue,
                            buyer_id = buyerIdValue,
                            total_price = totalPrice ?: 0.0
                        )
                    )
                    cleanFieldsEvent.value = Unit
                    statusEvent.postValue(STATUS_SUCCESS)
                }
            } else {
                statusEvent.postValue(STATUS_NO_SEATS)
            }
        }

    }

    fun clearStatusEvent() {
        statusEvent.value = null
    }

    private fun extractCinemaRoomNumber(room: String): Int? {
        val regex = Regex("\\d+")
        val matchResult = regex.find(room)
        return matchResult?.value?.toIntOrNull()
    }
}