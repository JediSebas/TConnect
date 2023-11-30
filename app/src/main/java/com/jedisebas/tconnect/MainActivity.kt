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

        binding.codeMainEt.requestFocus()

        binding.root.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.codeMainEt, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        binding.codeMainEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val code = editable.toString().trim()

                if (code.length == 13) {
                    searchAndShow(SearchFragment.CODE_SEARCH, code, null, null)
                }
            }
        })

        binding.searchBtn.setOnClickListener {
            val part: String = binding.fourCodeMainEt.text.toString().trim()
            val wn: String = binding.wNMainEt.text.toString().trim()

            if (part.isEmpty() && wn.isEmpty()) {
                searchAndShow(SearchFragment.NO_T_SEARCH, null, null, null)
            } else {
                searchAndShow(SearchFragment.PART_SEARCH, null, part, wn)
            }

        }

        binding.extraSearch.setOnClickListener {
            val intent = Intent(this, ExtraSearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun searchAndShow(flag: Int, codeToFind: String?, codePartToFind: String?, wnToFind: String?) {
        val fragment = SearchFragment.newInstance(1, flag, codeToFind, codePartToFind, wnToFind)
        fragment.show(supportFragmentManager, SearchFragment.TAG)
    }
}