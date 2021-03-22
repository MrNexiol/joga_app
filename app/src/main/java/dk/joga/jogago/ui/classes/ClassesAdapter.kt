package dk.joga.jogago.ui.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dk.joga.jogago.R
import dk.joga.jogago.api.Category
import dk.joga.jogago.databinding.ClassesRecyclerViewItemBinding

class ClassesAdapter (private var data: List<Category>) : RecyclerView.Adapter<ClassesAdapter.ViewHolder>() {

    class ViewHolder (
            val binding: ClassesRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClassesRecyclerViewItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
                .load(data[position].image)
                .fallback(R.drawable.placeholder_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.classThumbnailImageView)
        holder.binding.root.setOnClickListener {
            val action = ClassesFragmentDirections.actionClassesFragmentToCategoryFragment(data[position].id, data[position].name)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<Category>){
        this.data = data
        notifyDataSetChanged()
    }
}