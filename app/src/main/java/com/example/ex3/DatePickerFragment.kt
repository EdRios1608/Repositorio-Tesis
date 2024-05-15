package com.example.ex3

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePickerFragment (val listener :(day: Int, month: Int, year: Int) -> Unit):DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener(dayOfMonth,month,year)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val picker = DatePickerDialog(activity as Context, this, year, month,day)

        //No se puede seleccionar fechas futuras
        picker.datePicker.maxDate = calendar.timeInMillis

        //No se puede seleccionar fechas de 100 anios atras
        val hundredYearsAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -100)
        }
        picker.datePicker.minDate = hundredYearsAgo.timeInMillis
        return picker
    }

}