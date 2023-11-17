package com.jedisebas.tconnect.ticket

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jedisebas.tconnect.R
import com.jedisebas.tconnect.search.SearchItemViewModel.SearchItem

class TicketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        val searchItem = getParcelable()

        println(searchItem)
    }

    private fun getParcelable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("SearchItem", SearchItem::class.java)
        } else {
            intent?.getParcelableExtra("SearchItem")

        }
    }
}