package com.jedisebas.tconnect.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jedisebas.tconnect.OnItemClickListener
import com.jedisebas.tconnect.R
import com.jedisebas.tconnect.search.SearchItemViewModel

class TicketFragment(private val searchItem: SearchItemViewModel.SearchItem?) : DialogFragment(), OnItemClickListener {

    private val viewModel: TicketViewModel by viewModels()
    private lateinit var recyclerAdapter: TicketItemViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_ticket_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.ticket_recycler)
        val okButton = view.findViewById<Button>(R.id.ok_ticket)

        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)

            recyclerAdapter = TicketItemViewAdapter(this@TicketFragment)
            adapter = recyclerAdapter

            viewModel.dataList.observe(viewLifecycleOwner) {
                recyclerAdapter.submitList(it)
            }
        }

        okButton.setOnClickListener {
            if (recyclerAdapter.selectedId == -1) {
                Toast.makeText(context, requireContext().resources.getText(R.string.not_chose_field), Toast.LENGTH_SHORT).show()
            } else {

            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.insertNumbers()
    }

    override fun onItemClick(position: Int) {
        recyclerAdapter.setSelectedItem(position)
    }

    companion object {
        const val TAG = "TicketFragment"
    }
}