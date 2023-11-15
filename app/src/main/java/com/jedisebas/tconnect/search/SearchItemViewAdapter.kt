package com.jedisebas.tconnect.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jedisebas.tconnect.databinding.FragmentSearchBinding
import com.jedisebas.tconnect.placeholder.PlaceholderContent.PlaceholderItem
import com.jedisebas.tconnect.search.SearchItemContent.SearchItem

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class SearchItemViewAdapter(private val values: List<SearchItem>) : RecyclerView.Adapter<SearchItemViewAdapter.ViewHolder>() {

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

    inner class ViewHolder(binding: FragmentSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        val code: TextView = binding.itemCode
        val nw: TextView = binding.itemNW
        val wn: TextView = binding.itemWN
    }
}