package dk.joga.jogago.ui.liked

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.api.Class
import dk.joga.jogago.databinding.LikeableRecyclerViewItemBinding

class LikedAdapter(private var data: List<Class>) : RecyclerView.Adapter<LikedAdapter.ViewHolder>() {

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
        holder.binding.heartIcon.isSelected = true
        holder.binding.heartIcon.setOnClickListener {
            liked[position] = !liked[position]
            AppContainer.repository.toggleClassLike(data[position].id)
            holder.binding.heartIcon.isSelected = liked[position]
        }
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