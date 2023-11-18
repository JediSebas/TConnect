package com.jedisebas.tconnect.ticket

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jedisebas.tconnect.databinding.FragmentTicketBinding

class TicketItemViewAdapter : RecyclerView.Adapter<TicketItemViewAdapter.ViewHolder>() {

    private var values: List<TicketViewModel.TicketItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentTicketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.id.text = item.id.toString()
    }

    override fun getItemCount(): Int = values.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newValues: List<TicketViewModel.TicketItem>) {
        values = newValues
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: FragmentTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        val id: TextView = binding.ticketId
    }
}