package com.prograils.joga.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograils.joga.api.Journey
import com.prograils.joga.databinding.JourneysRecyclerViewItemBinding

class JourneysAdapter(private var data: List<Journey>) : RecyclerView.Adapter<JourneysAdapter.ViewHolder>() {

    class ViewHolder(
            val binding: JourneysRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = JourneysRecyclerViewItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.sss.text = data[position].toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<Journey>){
        this.data = data
        notifyDataSetChanged()
    }
}