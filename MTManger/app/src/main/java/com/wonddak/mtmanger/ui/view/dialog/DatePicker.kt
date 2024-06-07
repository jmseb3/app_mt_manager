package com.wonddak.mtmanger.ui.view.dialog

import android.app.DatePickerDialog
import android.content.Context
import java.util.Calendar

object DatePicker {
    fun showDefault(
        context: Context,
        onDateSetListener: DatePickerDialog.OnDateSetListener
    ) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context, 0, onDateSetListener, year, month, day
        ).show()
    }

    fun showDefault(
        context: Context,
        minDate: Long,
        maxDate: Long,
        onDateSetListener: DatePickerDialog.OnDateSetListener
    ) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val picker = DatePickerDialog(
            context, 0, onDateSetListener, year, month, day
        )
        picker.datePicker.apply {
            this.minDate = minDate
            this.maxDate = maxDate
        }
        picker.show()
    }

    fun showDefault(
        context: Context,
        minDate: Long,
        onDateSetListener: DatePickerDialog.OnDateSetListener
    ) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val picker = DatePickerDialog(
            context, 0, onDateSetListener, year, month, day
        )
        picker.datePicker.apply {
            this.minDate = minDate
        }
        picker.show()
    }
}