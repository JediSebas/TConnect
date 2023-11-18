package com.jedisebas.tconnect.ticket

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jedisebas.tconnect.databinding.ActivityTicketBinding
import com.jedisebas.tconnect.search.SearchItemViewModel

class TicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val searchItem: SearchItemViewModel.SearchItem? = getParcelable()

        showTicketFragment(searchItem)
    }

    private fun getParcelable(): SearchItemViewModel.SearchItem? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("SearchItem", SearchItemViewModel.SearchItem::class.java)
        } else {
            intent?.getParcelableExtra("SearchItem")
        }
    }

    private fun showTicketFragment(searchItem: SearchItemViewModel.SearchItem?) {
        val fragment = TicketFragment(searchItem)
        fragment.show(supportFragmentManager, TicketFragment.TAG)
    }
}