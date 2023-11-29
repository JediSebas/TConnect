package com.jedisebas.tconnect.extrasearch

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MotionEvent
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jedisebas.tconnect.R
import com.jedisebas.tconnect.databinding.ActivityExtrasearchBinding
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.Calendar

class ExtraSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExtrasearchBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtrasearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dateExtraEt.setText(currentDate())

        binding.dateExtraEt.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                showDatePickerDialog(binding.dateExtraEt)
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }

        binding.extraSearchBtn.setOnClickListener {
            val numberT = binding.numberTExtraEt.text.toString().trim()
            val date = binding.dateExtraEt.text.toString().trim()
            val wN = binding.wNExtraEt.text.toString().trim()

            if (numberT.isEmpty() || date.isEmpty()) {
                Toast.makeText(baseContext, getString(R.string.enter_number_and_date), Toast.LENGTH_SHORT).show()
            } else {
                if (validDateFormat(date)) {
                    searchAndShow(numberT.toInt(), date, wN)
                } else {
                    Toast.makeText(baseContext, getString(R.string.wrong_date_format), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun currentDate(): String {
        val calendar = Calendar.getInstance()
        val y: Int = calendar.get(Calendar.YEAR)
        val m: Int = calendar.get(Calendar.MONTH)
        val d: Int = calendar.get(Calendar.DAY_OF_MONTH)

        return "$y-${addLeadingZero(m + 1)}-${addLeadingZero(d)}"
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

    private fun validDateFormat(dateString: String): Boolean {
        return try {
            LocalDate.parse(dateString)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    private fun searchAndShow(numberT: Int, date: String, wN: String) {
        val fragment = ExtraSearchFragment.newInstance(1, numberT, date, wN)
        fragment.show(supportFragmentManager, ExtraSearchFragment.TAG)
    }
}