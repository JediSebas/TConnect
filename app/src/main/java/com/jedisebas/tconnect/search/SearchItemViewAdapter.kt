package com.jedisebas.tconnect.search

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jedisebas.tconnect.OnItemClickListener
import com.jedisebas.tconnect.databinding.FragmentSearchBinding
import com.jedisebas.tconnect.search.SearchItemViewModel.SearchItem

class SearchItemViewAdapter(private val clickListener: OnItemClickListener) : RecyclerView.Adapter<SearchItemViewAdapter.ViewHolder>() {

    private var values: List<SearchItem> = emptyList()
    var selectedId = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.code.text = item.code
        holder.nw.text = item.nw
        holder.wn.text = item.wn
        holder.setSelected(position == selectedId)
    }

    override fun getItemCount(): Int = values.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newValues: List<SearchItem>) {
        values = newValues
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedItem(position: Int) {
        selectedId = position
        notifyDataSetChanged()
    }

    fun getSearchItem(position: Int): SearchItem {
        return values[position]
    }

    inner class ViewHolder(binding: FragmentSearchBinding, private val clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private val parent: LinearLayout = binding.itemParent
        val code: TextView = binding.itemCode
        val nw: TextView = binding.itemNW
        val wn: TextView = binding.itemWN

        init {
            parent.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onItemClick(layoutPosition)
        }

        fun setSelected(isSelected: Boolean) {
            val typedValue = TypedValue()
            val theme = itemView.context.theme

            val isNightMode = (itemView.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            if (isNightMode) {
                theme.resolveAttribute(android.R.attr.listViewStyle, typedValue, true)
            } else {
                theme.resolveAttribute(android.R.attr.listViewStyle, typedValue, true)
            }

            val defaultColorPrimary = typedValue.data

            if (isSelected) {
                itemView.setBackgroundColor(Color.rgb(179,255,191))
            } else {
                itemView.setBackgroundColor(defaultColorPrimary)
            }
        }
    }
}