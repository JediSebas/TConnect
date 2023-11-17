package com.jedisebas.tconnect.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jedisebas.tconnect.R

class SearchFragment(code: String) : DialogFragment(), OnItemClickListener {

    private val viewModel: SearchItemViewModel by viewModels()
    private val code: Long = code.toLong()
    private var columnCount = 1
    private lateinit var recyclerAdapter: SearchItemViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.searchList)

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
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.insertItems(code)
    }

    override fun onItemClick(position: Int) {
        recyclerAdapter.setSelectedItem(position)
    }

    companion object {

        const val TAG = "SearchFragment"
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int, code: String) =
            SearchFragment(code).apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}