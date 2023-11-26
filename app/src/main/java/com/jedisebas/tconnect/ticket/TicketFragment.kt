package com.jedisebas.tconnect.ticket

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jedisebas.tconnect.OnItemClickListener
import com.jedisebas.tconnect.R
import com.jedisebas.tconnect.StartActivity
import com.jedisebas.tconnect.search.SearchItemViewModel

class TicketFragment : DialogFragment(), OnItemClickListener {

    private val viewModel: TicketViewModel by viewModels()
    private var searchItem: SearchItemViewModel.SearchItem? = null
    private lateinit var recyclerAdapter: TicketItemViewAdapter
    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            searchItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_SEARCH, SearchItemViewModel.SearchItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable(ARG_SEARCH)
            }
        }

        loadingDialog = AlertDialog.Builder(requireContext())
            .setMessage("Wykonywanie zapytania...")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

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
                viewModel.requestState.observe(viewLifecycleOwner) { state ->
                    println(state)
                    when (state) {
                        is RequestState.Loading -> showLoadingDialog()
                        is RequestState.Success -> showSuccessDialog(state.message)
                        is RequestState.Error -> showErrorDialog(state.errorMessage)
                    }
                }

                viewModel.changeNumberT(recyclerAdapter.getTicket(recyclerAdapter.selectedId), searchItem)
            }
        }

        return view
    }

    private fun showLoadingDialog() {
        loadingDialog.show()
    }

    private fun showSuccessDialog(message: String) {
        loadingDialog.dismiss()
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                backToStartActivity()
            }
            .create()
            .show()
    }

    private fun showErrorDialog(errorMessage: String) {
        loadingDialog.dismiss()
        AlertDialog.Builder(requireContext())
            .setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun backToStartActivity() {
        val intent = Intent(requireContext(), StartActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
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
        const val ARG_SEARCH = "searchItem"

        @JvmStatic
        fun newInstance(item: SearchItemViewModel.SearchItem) =
            TicketFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SEARCH, item)
                }
            }
    }
}