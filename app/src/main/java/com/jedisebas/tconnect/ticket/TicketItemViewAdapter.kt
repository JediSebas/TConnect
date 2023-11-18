package com.jedisebas.tconnect.ticket

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jedisebas.tconnect.OnItemClickListener
import com.jedisebas.tconnect.databinding.FragmentTicketBinding

class TicketItemViewAdapter(private val clickListener: OnItemClickListener) : RecyclerView.Adapter<TicketItemViewAdapter.ViewHolder>() {

    private var values: List<TicketViewModel.TicketItem> = emptyList()
    var selectedId = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentTicketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.id.text = item.id.toString()
        holder.setSelected(position == selectedId)
    }

    override fun getItemCount(): Int = values.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newValues: List<TicketViewModel.TicketItem>) {
        values = newValues
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedItem(position: Int) {
        selectedId = position
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: FragmentTicketBinding, private val clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private val parent = binding.ticketParent
        val id: TextView = binding.ticketId

        init {
            parent.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onItemClick(layoutPosition)
        }

        fun setSelected(isSelected: Boolean) {
            if (isSelected) {
                itemView.setBackgroundColor(Color.rgb(179,255,191))
            } else {
                itemView.setBackgroundColor(Color.rgb(200, 200, 200))
            }
        }
    }
}