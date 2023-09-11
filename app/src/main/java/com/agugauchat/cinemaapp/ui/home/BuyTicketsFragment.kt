package com.agugauchat.cinemaapp.ui.home

import android.R.layout.simple_spinner_dropdown_item
import android.R.layout.simple_spinner_item
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.agugauchat.cinemaapp.R
import com.agugauchat.cinemaapp.databinding.FragmentBuyTicketsBinding
import com.agugauchat.cinemaapp.ui.utils.UtilsUi.STATUS_CONVERSION_ERROR
import com.agugauchat.cinemaapp.ui.utils.UtilsUi.STATUS_INCOMPLETE_DATA
import com.agugauchat.cinemaapp.ui.utils.UtilsUi.STATUS_NO_SEATS
import com.agugauchat.cinemaapp.ui.utils.UtilsUi.STATUS_SUCCESS
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar

@AndroidEntryPoint
class BuyTicketsFragment : Fragment() {

    companion object {
        private const val DATE_PICKER_DIALOG_TAG = "date_picker_dialog_tag"
    }

    private lateinit var binding: FragmentBuyTicketsBinding
    private val viewModel: BuyTicketsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBuyTicketsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.movieList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val movieAdapter = ArrayAdapter(requireContext(), simple_spinner_item, it)
                movieAdapter.setDropDownViewResource(simple_spinner_dropdown_item)
                binding.movie.adapter = movieAdapter
            }
        }

        viewModel.cinemaInfo.observe(viewLifecycleOwner) {
            val roomName = getString(R.string.room_name)
            val rooms =
                (1..it.rooms_quantity).map { roomNumber -> String.format(roomName, roomNumber) }
            val adapter = ArrayAdapter(requireContext(), simple_spinner_item, rooms)
            adapter.setDropDownViewResource(simple_spinner_dropdown_item)
            binding.room.adapter = adapter
        }

        binding.date.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.date_title))
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now())
                        .setStart(Calendar.getInstance().timeInMillis)
                        .build()
                )

            val datePicker = builder.build()
            val formatter = DateTimeFormatter.ofPattern(getString(R.string.date_format))

            datePicker.addOnPositiveButtonClickListener {
                val utcDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
                val formattedDate = utcDate.format(formatter)

                binding.date.setText(formattedDate)
                viewModel.date.value = formattedDate
            }

            datePicker.show(parentFragmentManager, DATE_PICKER_DIALOG_TAG)
        }

        binding.quantity.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                viewModel.quantity.value = s.toString()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        viewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.totalPrice.visibility = View.VISIBLE
            binding.totalPrice.text = String.format(getString(R.string.total_price), it)
        }

        binding.buyButton.setOnClickListener {
            showConfirmDialog()
        }

        viewModel.statusEvent.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                var text = getString(R.string.status_generic_error_message)
                when (status) {
                    STATUS_SUCCESS -> {
                        text = getString(R.string.status_success_message)
                    }

                    STATUS_NO_SEATS -> {
                        text = getString(R.string.status_no_seats_message)
                    }

                    STATUS_CONVERSION_ERROR -> {
                        text = getString(R.string.status_conversion_error_message)
                    }

                    STATUS_INCOMPLETE_DATA -> {
                        text = getString(R.string.status_incomplete_data_message)
                    }
                }
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.cleanFieldsEvent.observe(viewLifecycleOwner) {
            // The buyer id value is intentionally not cleaned, to save user time
            binding.movie.setSelection(0)
            binding.room.setSelection(0)
            binding.date.setText("")
            binding.quantity.setText(getString(R.string.default_tickets_quantity))
        }

        return root
    }

    private fun showConfirmDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(getString(R.string.buy_confirm))
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                    buyTickets()
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearStatusEvent()
    }

    private fun buyTickets() {
        val movie = binding.movie.selectedItem.toString()
        val room = binding.room.selectedItem.toString()
        val date = binding.date.text.toString()
        val quantity = binding.quantity.text.toString()
        val buyerId = binding.buyerId.text.toString()

        viewModel.buyTickets(movie, room, date, quantity, buyerId)
    }
}