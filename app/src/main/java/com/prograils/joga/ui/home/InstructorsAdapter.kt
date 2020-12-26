package com.prograils.joga.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prograils.joga.api.Instructor
import com.prograils.joga.databinding.InstructorsRecyclerViewItemBinding

class InstructorsAdapter(private var data: List<Instructor>, private var context: Fragment) : RecyclerView.Adapter<InstructorsAdapter.ViewHolder>() {

    class ViewHolder(
            val binding: InstructorsRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InstructorsRecyclerViewItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmp = data[position].name.split(" ")
        var splitName = ""
        for (word in tmp){
            splitName += "$word\n"
        }
        Glide.with(context).load(data[position].avatar_url).into(holder.binding.trainerAvatar)
        holder.binding.trainerName.text = splitName
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<Instructor>){
        this.data = data
        notifyDataSetChanged()
    }
}