package com.agugauchat.cinemaapp.ui.bookings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agugauchat.cinemaapp.domain.BookingsUseCases
import com.agugauchat.cinemaapp.domain.model.Booking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val bookingsUseCases: BookingsUseCases
) : ViewModel() {

    val bookingList = MutableLiveData<List<Booking>>()

    fun onCreate() {
        viewModelScope.launch {
            val result = bookingsUseCases.getBookings()

            bookingList.postValue(result)
        }
    }

    fun deleteBooking(id: Int) {
        viewModelScope.launch {
            bookingsUseCases.deleteBooking(id)

            val updatedBookings = bookingsUseCases.getBookings()
            bookingList.postValue(updatedBookings)
        }
    }
}