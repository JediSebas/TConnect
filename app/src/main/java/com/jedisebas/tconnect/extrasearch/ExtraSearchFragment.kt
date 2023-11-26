package com.jedisebas.tconnect.extrasearch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jedisebas.tconnect.OnItemClickListener
import com.jedisebas.tconnect.R
import com.jedisebas.tconnect.search.SearchItemViewAdapter
import com.jedisebas.tconnect.search.SearchItemViewModel
import com.jedisebas.tconnect.ticket.TicketActivity
import java.time.LocalDate

class ExtraSearchFragment : DialogFragment(), OnItemClickListener {

    private val viewModel: ExtraSearchViewModel by viewModels()
    private var columnCount = 1
    private var numberT: Int = 0
    private var date: String = "0000-00-00"
    private var wN: String = "W0"
    private lateinit var recyclerAdapter: SearchItemViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            numberT = it.getInt(ARG_NUMBER_T)
            date = it.getString(ARG_DATE).toString()
            wN = it.getString(ARG_WN).toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.search_list)
        val okButton = view.findViewById<Button>(R.id.ok_after_select)

        if (recyclerView is RecyclerView) {
            with(recyclerView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                recyclerAdapter = SearchItemViewAdapter(this@ExtraSearchFragment)
                adapter = recyclerAdapter

                viewModel.dataList.observe(viewLifecycleOwner) {
                    recyclerAdapter.submitList(it)
                }

                viewModel.isNotConnected.observe(viewLifecycleOwner) {
                    if (it) {
                        Toast.makeText(context, getString(R.string.error_connect_database), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        okButton.setOnClickListener{
            if (recyclerAdapter.selectedId == -1) {
                Toast.makeText(context, requireContext().resources.getText(R.string.not_chose_field), Toast.LENGTH_SHORT).show()
            } else {
                val item: SearchItemViewModel.SearchItem = recyclerAdapter.getSearchItem(recyclerAdapter.selectedId)
                val intent = Intent(context, TicketActivity::class.java)
                intent.putExtra("SearchItem", item)
                startActivity(intent)

                dismiss()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.insertItems(numberT, LocalDate.parse(date), wN)
    }

    override fun onItemClick(position: Int) {
        recyclerAdapter.setSelectedItem(position)
    }

    companion object {

        const val TAG = "ExtraSearchFragment"
        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_NUMBER_T = "numberT"
        const val ARG_DATE = "date"
        const val ARG_WN = "wN"

        @JvmStatic
        fun newInstance(columnCount: Int, numberT: Int, date: String, wN: String) =
            ExtraSearchFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putInt(ARG_NUMBER_T, numberT)
                    putString(ARG_DATE, date)
                    putString(ARG_WN, wN)
                }
            }
    }
}