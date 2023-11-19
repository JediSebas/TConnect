package com.jedisebas.tconnect.ticket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
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
import com.jedisebas.tconnect.StartActivity
import com.jedisebas.tconnect.api.ApiClient
import com.jedisebas.tconnect.api.ProductDto
import com.jedisebas.tconnect.search.SearchItemViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar

class TicketFragment : DialogFragment(), OnItemClickListener {

    private val viewModel: TicketViewModel by viewModels()
    private var searchItem: SearchItemViewModel.SearchItem? = null
    private lateinit var recyclerAdapter: TicketItemViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            searchItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_SEARCH, SearchItemViewModel.SearchItem::class.java)
            } else {
                it.getParcelable(ARG_SEARCH)
            }
        }
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
                changeNumberT(recyclerAdapter.getTicket(recyclerAdapter.selectedId))
                dismiss()
                backToStartActivity()
            }
        }

        return view
    }

    @SuppressLint("SimpleDateFormat")
    fun changeNumberT(ticket: TicketViewModel.TicketItem) {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formattedDate = dateFormat.format(currentDate)

        val product: ProductDto? = searchItem?.let {
            ProductDto(
                it.code.toLong(),
                it.name,
                it.nw.toInt(),
                it.wn,
                ticket.id,
                formattedDate
            )
        }

        val api = ApiClient.createApi()

        if (product != null) {
            val call = api.updateOne(product)
            call.enqueue(object : Callback<ProductDto> {
                override fun onResponse(
                    call: Call<ProductDto>,
                    response: Response<ProductDto>
                ) {
                    if (response.isSuccessful) {
                        println("Successful")
                    } else {
                        println("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ProductDto>, t: Throwable) {
                    println("Mission failed. Error: ${t.message}")
                }
            })
        }
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