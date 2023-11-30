package com.jedisebas.tconnect.search

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
import com.jedisebas.tconnect.ticket.TicketActivity

class SearchFragment : DialogFragment(), OnItemClickListener {

    private val viewModel: SearchItemViewModel by viewModels()
    private var columnCount = 1
    private var flag = 0
    private var code: Long? = 0
    private var part: Long? = 0
    private var wn: String? = ""
    private lateinit var recyclerAdapter: SearchItemViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            flag = it.getInt(ARG_FLAG)
            code = it.getString(ARG_CODE)?.toLong()
            part = it.getString(ARG_CODE_PART)?.toLong()
            wn = it.getString(ARG_WN)
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
                recyclerAdapter = SearchItemViewAdapter(this@SearchFragment)
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

        viewModel.insertItems(flag, code, part, wn)
    }

    override fun onItemClick(position: Int) {
        recyclerAdapter.setSelectedItem(position)
    }

    companion object {

        const val TAG = "SearchFragment"
        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_FLAG = "search-type"
        const val ARG_CODE = "code"
        const val ARG_CODE_PART = "part"
        const val ARG_WN = "wn"

        const val CODE_SEARCH = 1
        const val PART_SEARCH = 2
        const val NO_T_SEARCH = 3

        @JvmStatic
        fun newInstance(columnCount: Int, flag: Int, code: String?, part: String?, wn: String?) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putInt(ARG_FLAG, flag)
                    putString(ARG_CODE, code)
                    putString(ARG_CODE_PART, part)
                    putString(ARG_WN, wn)
                }
            }
    }
}