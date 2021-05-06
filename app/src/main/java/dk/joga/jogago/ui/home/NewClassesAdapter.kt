package dk.joga.jogago.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dk.joga.jogago.R
import dk.joga.jogago.api.Class
import dk.joga.jogago.databinding.HomeRectangleRecyclerViewItemBinding

class NewClassesAdapter : RecyclerView.Adapter<NewClassesAdapter.ViewHolder>() {

    private var data: List<Class> = listOf()

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
        holder.binding.homeItemWatchedIcon.visibility = if (data[position].watched) View.VISIBLE else View.GONE
        holder.binding.homeItemName.text = data[position].title
        holder.binding.homeItemCategory.text = data[position].categories.joinToString()
        holder.binding.homeItemReleaseDate.text = data[position].releaseDate
        holder.binding.homeItemReleaseDate.visibility = View.VISIBLE
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