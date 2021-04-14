package dk.joga.jogago.ui.journeyDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dk.joga.jogago.R
import dk.joga.jogago.api.Class
import dk.joga.jogago.databinding.JourneyFirstItemBinding
import dk.joga.jogago.databinding.LikeableRecyclerViewItemBinding

class JourneyDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data: List<Class> = listOf()
    private var liked: Array<Boolean> = emptyArray()
    private var idList = mutableListOf<String>()

    class ViewHolderFirst(
        val binding: JourneyFirstItemBinding) : RecyclerView.ViewHolder(binding.root)

    class ViewHolderRest(
        val binding: LikeableRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding = JourneyFirstItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderFirst(binding)
        } else {
            val binding = LikeableRecyclerViewItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderRest(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            holder as ViewHolderFirst
            bindFirstItem(holder)
        } else {
            holder as ViewHolderRest
            bindItem(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    fun setData(data: List<Class>){
        this.data = data
        liked = Array(data.size) { false }
        data.forEachIndexed { i, c ->
            idList.add(i, c.id)
        }
        notifyDataSetChanged()
    }

    private fun bindFirstItem(holder: ViewHolderFirst) {
        val position = 0
        Glide.with(holder.itemView)
            .load(data[position].thumbnailUrl)
            .fallback(R.drawable.placeholder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.journeyFirstItemThumbnail)
        holder.binding.journeyFirstItemThubnailCard.setOnClickListener {
            val action = JourneyDetailsFragmentDirections.actionJourneyDetailsFragmentToClassDetailsFragment(data[position].id, idList.toTypedArray())
            holder.itemView.findNavController().navigate(action)
        }
        holder.binding.journeyFirstItemName.text = data[position].title
        holder.binding.journeyFirstItemInstructor.text = data[position].instructor.name
        holder.binding.journeyFirstItemDuration.text = holder.itemView.context.getString(R.string.min, data[position].duration)
        holder.binding.journeyFirstItemDescription.text = data[position].description
    }

    private fun bindItem(holder: ViewHolderRest, position: Int) {
        Glide.with(holder.itemView)
            .load(data[position].thumbnailUrl)
            .fallback(R.drawable.placeholder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.likedClassThumbnail)
        @Suppress("SENSELESS_COMPARISON")
        liked[position] = data[position].userLike.classId != null
        holder.binding.heartIcon.isSelected = liked[position]
        holder.binding.likedClassName.text = data[position].title
        holder.binding.likedClassFocus.text = data[position].focus
        holder.binding.likedClassDuration.text = holder.itemView.context.getString(R.string.min, data[position].duration)
        holder.binding.likedClassInstructorName.text = data[position].instructor.name
        if (!data[position].watchable){
            val color = ContextCompat.getColor(holder.itemView.context, R.color.secondary_text)
            holder.binding.likedClassThumbnail.alpha = 0.3F
            holder.binding.likedClassName.setTextColor(color)
            holder.binding.likedClassFocus.setTextColor(color)
            holder.binding.likedClassDuration.setTextColor(color)
            holder.binding.likedClassInstructorName.setTextColor(color)
        } else {
            holder.binding.root.setOnClickListener {
                val action = JourneyDetailsFragmentDirections.actionJourneyDetailsFragmentToClassDetailsFragment(data[position].id, idList.toTypedArray())
                holder.itemView.findNavController().navigate(action)
            }
        }
    }
}