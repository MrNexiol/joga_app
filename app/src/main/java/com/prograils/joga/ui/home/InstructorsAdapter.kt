package com.prograils.joga.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prograils.joga.api.Instructor
import com.prograils.joga.databinding.InstructorsRecyclerViewItemBinding

class InstructorsAdapter(private var data: List<Instructor>) : RecyclerView.Adapter<InstructorsAdapter.ViewHolder>() {

    class ViewHolder(
            val binding: InstructorsRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InstructorsRecyclerViewItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.recyclerTest.text = data[position].toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<Instructor>){
        this.data = data
        notifyDataSetChanged()
    }
}