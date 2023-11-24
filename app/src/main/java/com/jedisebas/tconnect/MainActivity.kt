package com.jedisebas.tconnect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.jedisebas.tconnect.databinding.ActivityMainBinding
import com.jedisebas.tconnect.extrasearch.ExtraSearchActivity
import com.jedisebas.tconnect.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.codeEt.requestFocus()

        binding.root.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.codeEt, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        binding.codeEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val text = editable.toString()

                if (text.length == 13) {
                    searchAndShow(text)
                }
            }
        })

        binding.extraSearch.setOnClickListener {
            val intent = Intent(this, ExtraSearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun searchAndShow(codeToFind: String) {
        val fragment = SearchFragment.newInstance(1, codeToFind)
        fragment.show(supportFragmentManager, SearchFragment.TAG)
    }
}