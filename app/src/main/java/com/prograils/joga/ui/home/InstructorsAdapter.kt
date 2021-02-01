package com.prograils.joga.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.prograils.joga.api.Instructor
import com.prograils.joga.databinding.HomeInstructorsRecyclerViewItemBinding

class InstructorsAdapter(private var data: List<Instructor>) : RecyclerView.Adapter<InstructorsAdapter.ViewHolder>() {

    class ViewHolder(
            val binding: HomeInstructorsRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeInstructorsRecyclerViewItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmp = data[position].name.split(" ")
        var splitName = ""
        for (word in tmp){
            splitName += "$word\n"
        }
        Glide.with(holder.itemView)
                .load(data[position].avatar_url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.trainerAvatar)
        holder.binding.trainerName.text = splitName
        holder.binding.root.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToTrainerDetailFragment(data[position].id)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<Instructor>){
        this.data = data
        notifyDataSetChanged()
    }
}