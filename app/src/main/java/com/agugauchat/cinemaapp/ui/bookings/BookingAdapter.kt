package com.agugauchat.cinemaapp.ui.bookings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agugauchat.cinemaapp.R
import com.agugauchat.cinemaapp.databinding.BookingItemLayoutBinding
import com.agugauchat.cinemaapp.domain.model.Booking

class BookingAdapter(
    private val context: Context,
    private val bookings: List<Booking>,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: BookingItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(booking: Booking) {
            with(binding) {
                bookingTitle.text = String.format(
                    context.getString(R.string.booking_title_holder),
                    booking.movie,
                    booking.cinema_room
                )
                bookingDate.text = booking.date
                bookingQuantity.text =
                    String.format(context.getString(R.string.quantity_holder), booking.quantity)
                buyerId.text =
                    String.format(context.getString(R.string.buyer_id_holder), booking.buyer_id)
                bookingPrice.text =
                    String.format(context.getString(R.string.price_holder), booking.total_price)
                delete.setOnClickListener { onClickListener.onRemoveClick(booking.id) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookingItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bookings[position])
    }

    override fun getItemCount() = bookings.size

    override fun getItemViewType(position: Int): Int {
        return R.layout.booking_item_layout
    }

    interface OnClickListener {
        fun onRemoveClick(bookingId: Int)
    }
}