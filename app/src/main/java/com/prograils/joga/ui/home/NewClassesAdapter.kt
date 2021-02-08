package com.prograils.joga.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.prograils.joga.R
import com.prograils.joga.api.Class
import com.prograils.joga.databinding.HomeRectangleRecyclerViewItemBinding

class NewClassesAdapter(private var data: List<Class>) : RecyclerView.Adapter<NewClassesAdapter.ViewHolder>() {

    class ViewHolder(
            val binding: HomeRectangleRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeRectangleRecyclerViewItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
                .load(data[position].thumbnailUrl)
                .fallback(R.drawable.placeholder_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.homeItemThumbnail)
        holder.binding.homeItemName.text = data[position].title
        holder.binding.root.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToClassDetailsFragment(data[position].id)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<Class>){
        this.data = data
        notifyDataSetChanged()
    }
}