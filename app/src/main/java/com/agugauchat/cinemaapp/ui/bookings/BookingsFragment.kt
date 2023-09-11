package com.agugauchat.cinemaapp.ui.bookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.agugauchat.cinemaapp.databinding.FragmentBookingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingsFragment : Fragment() {
    private lateinit var binding: FragmentBookingsBinding
    private val viewModel: BookingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.onCreate()
        viewModel.bookingList.observe(viewLifecycleOwner) {
            val adapter = BookingAdapter(requireContext(), it, removeBookingClickListener())
            binding.bookingsRecyclerView.adapter = adapter
            binding.bookingsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        return root
    }

    private fun removeBookingClickListener(): BookingAdapter.OnClickListener {
        return object : BookingAdapter.OnClickListener {
            override fun onRemoveClick(bookingId: Int) {
                showConfirmDialog(bookingId)
            }
        }
    }

    private fun showConfirmDialog(bookingId: Int) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(getString(com.agugauchat.cinemaapp.R.string.delete_confirm))
                .setNegativeButton(getString(com.agugauchat.cinemaapp.R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(getString(com.agugauchat.cinemaapp.R.string.accept)) { dialog, _ ->
                    removeTickets(bookingId)
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun removeTickets(bookingId: Int) {
        viewModel.deleteBooking(bookingId)
    }
}