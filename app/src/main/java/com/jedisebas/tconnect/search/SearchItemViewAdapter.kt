package com.jedisebas.tconnect.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jedisebas.tconnect.databinding.FragmentSearchBinding
import com.jedisebas.tconnect.search.SearchItemViewModel.SearchItem

class SearchItemViewAdapter : RecyclerView.Adapter<SearchItemViewAdapter.ViewHolder>() {

    private var values: List<SearchItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.code.text = item.code
        holder.nw.text = item.nw
        holder.wn.text = item.wn
    }

    override fun getItemCount(): Int = values.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newValues: List<SearchItem>) {
        values = newValues
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: FragmentSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        val code: TextView = binding.itemCode
        val nw: TextView = binding.itemNW
        val wn: TextView = binding.itemWN
    }
}