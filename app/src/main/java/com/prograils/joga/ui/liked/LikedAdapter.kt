package com.prograils.joga.ui.liked

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prograils.joga.R
import com.prograils.joga.api.Class
import com.prograils.joga.databinding.LikedClassRecyclerViewItemBinding

class LikedAdapter(private var data: List<Class>) : RecyclerView.Adapter<LikedAdapter.ViewHolder>() {

    class ViewHolder(
    val binding: LikedClassRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LikedClassRecyclerViewItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load(data[position].thumbnailUrl).into(holder.binding.likedClassThumbnail)
        holder.binding.likedClassName.text = data[position].title
        holder.binding.likedClassFocus.text = data[position].focus
        holder.binding.likedClassDuration.text = holder.itemView.context.getString(R.string.min, data[position].duration)
        holder.binding.likedClassInstructorName.text = data[position].instructor.name
        holder.binding.root.setOnClickListener {
            val action = LikedFragmentDirections.actionLikedFragmentToClassDetailsFragment(data[position].id)
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