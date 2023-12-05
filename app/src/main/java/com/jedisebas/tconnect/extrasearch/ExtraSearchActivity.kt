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

        binding.dateExtraEt.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                showDatePickerDialog(binding.dateExtraEt)
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }

        binding.extraSearchBtn.setOnClickListener {
            val code = binding.codeExtraEt.text.toString().trim()
            val numberT = binding.numberTExtraEt.text.toString().trim()
            val date = binding.dateExtraEt.text.toString().trim()
            val nW = binding.nWExtraEt.text.toString().trim()
            val part = binding.fourCodeExtraEt.text.toString().trim()

            if (date.isNotEmpty()) {
                if (validDateFormat(date)) {
                    beforeSearchAndShow(code, numberT, date, nW, part)
                } else {
                    Toast.makeText(baseContext, getString(R.string.wrong_date_format), Toast.LENGTH_SHORT).show()
                }
            } else if (date.isEmpty()) {
                beforeSearchAndShow(code, numberT, date, nW, part)
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

    private fun validDateFormat(dateString: String): Boolean {
        return try {
            LocalDate.parse(dateString)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    private fun beforeSearchAndShow(code: String?, numberT: String?, date: String?, nW: String?, part: String?) {
        if (code!!.isNotEmpty()) {
            searchAndShow(ExtraSearchFragment.CODE_SEARCH, code, numberT, date, nW, part)
        } else if (part!!.isNotEmpty()) {
            searchAndShow(ExtraSearchFragment.PART_SEARCH, code, numberT, date, nW, part)
        } else if (code.isEmpty() && part.isEmpty() && numberT.isNullOrEmpty() &&
            date.isNullOrEmpty() && nW.isNullOrEmpty()) {
            searchAndShow(ExtraSearchFragment.T_SEARCH, code, numberT, date, nW, part)
        } else {
            searchAndShow(ExtraSearchFragment.WITHOUT_CODE_SEARCH, code, numberT, date, nW, part)
        }
    }

    private fun searchAndShow(flag: Int, code: String?, numberT: String?, date: String?, wN: String?, part: String?) {
        val fragment = ExtraSearchFragment.newInstance(1, flag, code, numberT, date, wN, part)
        fragment.show(supportFragmentManager, ExtraSearchFragment.TAG)
    }
}