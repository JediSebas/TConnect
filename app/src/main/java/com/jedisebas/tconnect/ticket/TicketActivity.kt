package com.jedisebas.tconnect.ticket

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jedisebas.tconnect.databinding.ActivityTicketBinding
import com.jedisebas.tconnect.search.SearchItemViewModel.SearchItem

class TicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val searchItem: SearchItem? = getParcelable()

        setTextViewForSearchItem(searchItem)

        binding.searchH.setOnClickListener {
            showTicketFragment(searchItem)
        }

        showTicketFragment(searchItem)
    }

    private fun getParcelable(): SearchItem? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("SearchItem", SearchItem::class.java)
        } else {
            intent?.getParcelableExtra("SearchItem")
        }
    }

    private fun setTextViewForSearchItem(searchItem: SearchItem?) {
        if (searchItem != null) {
            binding.codeH.text = searchItem.code
            binding.nWH.text = searchItem.nw
            binding.wNH.text = searchItem.wn
        }
    }

    private fun showTicketFragment(searchItem: SearchItem?) {
        val fragment = TicketFragment.newInstance(searchItem!!)
        fragment.show(supportFragmentManager, TicketFragment.TAG)
    }
}