package dk.joga.jogago.ui.categoryDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.api.Class
import dk.joga.jogago.databinding.CategoryFirstItemBinding
import dk.joga.jogago.databinding.LikeableRecyclerViewItemBinding

class CategoryDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data = mutableListOf<Class>()
    private var liked: Array<Boolean> = emptyArray()

    class ViewHolderFirst(
            val binding: CategoryFirstItemBinding) : RecyclerView.ViewHolder(binding.root)

    class ViewHolderRest(
            val binding: LikeableRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding = CategoryFirstItemBinding
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
        this.data.removeAll(this.data)
        this.data.addAll(data)
        liked = Array(this.data.size) { false }
        notifyDataSetChanged()
    }

    fun addData(data: List<Class>) {
        val tmp = this.data.count()
        this.data.addAll(data)
        liked = Array(this.data.size) { false }
        notifyItemInserted(tmp)
    }

    private fun bindFirstItem(holder: ViewHolderFirst) {
        val position = 0
        Glide.with(holder.itemView)
            .load(data[position].thumbnailUrl)
            .fallback(R.drawable.placeholder_image)
            .transform(CenterCrop(),RoundedCorners(holder.itemView.resources.getDimensionPixelSize(R.dimen.card_radius)))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.categoryFirstItemThumbnail)
        holder.binding.categoryFirstItemThumbnail.setOnClickListener {
            val action = CategoryDetailsFragmentDirections.actionCategoryFragmentToClassDetailsFragment(data[position].id)
            holder.itemView.findNavController().navigate(action)
        }
        @Suppress("SENSELESS_COMPARISON")
        liked[position] = data[position].userLike.classId != null
        holder.binding.categoryFirstItemWatchedIcon.visibility = if (data[position].watched) View.VISIBLE else View.GONE
        holder.binding.categoryFirstItemLikeIcon.isSelected = liked[position]
        holder.binding.categoryFirstItemLikeIcon.setOnClickListener {
            liked[position] = !liked[position]
            AppContainer.repository.toggleClassLike(data[position].id)
            holder.binding.categoryFirstItemLikeIcon.isSelected = liked[position]
        }
        holder.binding.categoryFirstItemName.text = data[position].title
        holder.binding.categoryFirstItemInstructorName.text = data[position].instructor.name
        holder.binding.categoryFirstItemDuration.text = holder.itemView.resources.getString(R.string.min, data[position].duration)
        holder.binding.categoryFirstItemDescription.text = data[position].description
    }

    private fun bindItem(holder: ViewHolderRest, position: Int) {
        Glide.with(holder.itemView)
            .load(data[position].thumbnailUrl)
            .fallback(R.drawable.placeholder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.likedClassThumbnail)
        holder.binding.likedClassName.text = data[position].title
        @Suppress("SENSELESS_COMPARISON")
        liked[position] = data[position].userLike.classId != null
        holder.binding.heartIcon.isSelected = liked[position]
        holder.binding.heartIcon.setOnClickListener {
            liked[position] = !liked[position]
            AppContainer.repository.toggleClassLike(data[position].id)
            holder.binding.heartIcon.isSelected = liked[position]
        }
        holder.binding.likedClassFocus.text = data[position].focus
        holder.binding.likedClassDuration.text = holder.itemView.context.getString(R.string.min, data[position].duration)
        holder.binding.likedClassInstructorName.text = data[position].instructor.name
        holder.binding.root.setOnClickListener {
            val action = CategoryDetailsFragmentDirections.actionCategoryFragmentToClassDetailsFragment(data[position].id)
            holder.itemView.findNavController().navigate(action)
        }
        if (position == itemCount-1) {
            holder.binding.divider10.visibility = View.GONE
        } else {
            holder.binding.divider10.visibility = View.VISIBLE
        }
    }
}