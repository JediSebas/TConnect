package com.jedisebas.tconnect.extrasearch

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MotionEvent
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jedisebas.tconnect.databinding.ActivityExtrasearchBinding
import java.util.Calendar

class ExtraSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExtrasearchBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtrasearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dateEt.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                showDatePickerDialog(binding.dateEt)
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }

        binding.extraSearchBtn.setOnClickListener {
            val numberT = binding.numberTEt.text.trim()
            val date = binding.dateEt.text.trim()
            val wN = binding.wNEt.text.trim()

            if (numberT.isEmpty() || date.isEmpty()) {
                Toast.makeText(baseContext, "Podaj numer i datę!", Toast.LENGTH_SHORT).show()
            } else {

            }
        }
    }

    private fun showDatePickerDialog(editTextDate: EditText) {
        val calendar = Calendar.getInstance()
        val y = calendar.get(Calendar.YEAR)
        val m = calendar.get(Calendar.MONTH)
        val d = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = "$year-${addLeadingZero(month + 1)}-${addLeadingZero(dayOfMonth)}"
                editTextDate.setText(selectedDate)
            },
            y, m, d
        )

        datePickerDialog.show()
    }

    private fun addLeadingZero(number: Int): String {
        val result = StringBuilder()
        if (number < 10) {
            result.append("0")
        }

        return result.append(number).toString()
    }
}