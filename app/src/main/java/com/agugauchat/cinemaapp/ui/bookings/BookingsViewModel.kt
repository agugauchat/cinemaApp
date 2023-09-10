package com.agugauchat.cinemaapp.ui.bookings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agugauchat.cinemaapp.domain.DeleteBookingUseCase
import com.agugauchat.cinemaapp.domain.GetBookingsUseCase
import com.agugauchat.cinemaapp.domain.model.Booking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val getBookingsUseCase: GetBookingsUseCase,
    private val deleteBookingUseCase: DeleteBookingUseCase
) : ViewModel() {

    val bookingList = MutableLiveData<List<Booking>>()

    fun onCreate() {
        viewModelScope.launch {
            val result = getBookingsUseCase()

            bookingList.postValue(result)
        }
    }

    fun deleteBooking(id : Int) {
        viewModelScope.launch {
            deleteBookingUseCase(id)

            val updatedBookings = getBookingsUseCase()
            bookingList.postValue(updatedBookings)
        }
    }
}