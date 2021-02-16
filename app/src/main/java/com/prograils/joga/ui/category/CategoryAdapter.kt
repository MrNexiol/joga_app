package com.prograils.joga.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.prograils.joga.R
import com.prograils.joga.Repository
import com.prograils.joga.api.Class
import com.prograils.joga.databinding.LikeableRecyclerViewItemBinding

class CategoryAdapter(private var data: List<Class>, private val repository: Repository, private val token: String) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var liked = mutableListOf<Boolean>()

    class ViewHolder(
    val binding: LikeableRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LikeableRecyclerViewItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        liked.add(position, true)
        Glide.with(holder.itemView)
            .load(data[position].thumbnailUrl)
            .fallback(R.drawable.placeholder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.likedClassThumbnail)
        holder.binding.likedClassName.text = data[position].title
        @Suppress("SENSELESS_COMPARISON")
        if (data[position].userLike.classId != null){
            liked[position] = true
            holder.binding.heartIcon.setImageResource(R.drawable.heart_liked_icon)
        } else {
            liked[position] = false
            holder.binding.heartIcon.setImageResource(R.drawable.heart_not_liked)
        }
        holder.binding.heartIcon.setOnClickListener {
            liked[position] = !liked[position]
            repository.toggleClassLike(token, data[position].id)
            if (liked[position]){
                holder.binding.heartIcon.setImageResource(R.drawable.heart_liked_icon)
            } else {
                holder.binding.heartIcon.setImageResource(R.drawable.heart_not_liked)
            }
        }
        holder.binding.likedClassFocus.text = data[position].focus
        holder.binding.likedClassDuration.text = holder.itemView.context.getString(R.string.min, data[position].duration)
        holder.binding.likedClassInstructorName.text = data[position].instructor.name
        holder.binding.root.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentToClassDetailsFragment(data[position].id)
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