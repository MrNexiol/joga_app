package com.prograils.joga.ui.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prograils.joga.api.Class
import com.prograils.joga.databinding.ClassesRecyclerViewItemBinding

class ClassesAdapter (private var data: List<Class>) : RecyclerView.Adapter<ClassesAdapter.ViewHolder>() {

    class ViewHolder (
            val binding: ClassesRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClassesRecyclerViewItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load(data[position].thumbnailUrl).into(holder.binding.classThumbnailImageView)
        holder.binding.classNameTextView.text = data[position].title
        holder.binding.root.setOnClickListener {
            val action = ClassesFragmentDirections.actionClassesFragmentToClassDetailsFragment(data[position].id)
            it.findNavController().navigate(action)
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