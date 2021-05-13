package dk.joga.jogago.ui.trainerDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.api.Class
import dk.joga.jogago.databinding.InfiniteScrollSpinnerBinding
import dk.joga.jogago.databinding.LikeableRecyclerViewItemBinding

private const val REST_VIEW_TYPE = 1
private const val LOADING_VIEW_TYPE = 2

class TrainerDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isMore = false
    private var data = mutableListOf<Class?>()
    private var liked: Array<Boolean> = emptyArray()

    class ViewHolder(
            val binding: LikeableRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    class ViewHolderLoading(
            val binding: InfiniteScrollSpinnerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LOADING_VIEW_TYPE) {
            val binding = InfiniteScrollSpinnerBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderLoading(binding)
        } else {
            val binding = LikeableRecyclerViewItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            Glide.with(holder.itemView)
                    .load(data[position]!!.thumbnailUrl)
                    .fallback(R.drawable.placeholder_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.binding.likedClassThumbnail)
            holder.binding.likedClassWatchedIcon.visibility = if (data[position]!!.watched) View.VISIBLE else View.GONE
            holder.binding.likedClassName.text = data[position]!!.title
            @Suppress("SENSELESS_COMPARISON")
            liked[position] = data[position]!!.userLike.classId != null
            holder.binding.heartIcon.isSelected = liked[position]
            holder.binding.heartIcon.setOnClickListener {
                liked[position] = !liked[position]
                AppContainer.repository.toggleClassLike(data[position]!!.id)
                holder.binding.heartIcon.isSelected = liked[position]
            }
            holder.binding.likedClassFocus.text = data[position]!!.focus
            holder.binding.likedClassDuration.text = holder.itemView.context.getString(R.string.category_with_duration, data[position]!!.categories.first().name, data[position]!!.duration)
            holder.binding.likedClassInstructorName.text = data[position]!!.instructor.name
            holder.binding.root.setOnClickListener {
                val action = TrainerDetailFragmentDirections.actionTrainerDetailFragmentToClassDetailsFragment(data[position]!!.id)
                holder.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return  data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && isMore) {
            LOADING_VIEW_TYPE
        } else {
            REST_VIEW_TYPE
        }
    }

    fun setData(data: List<Class>, isMore: Boolean) {
        this.isMore = isMore
        this.data.removeAll(this.data)
        this.data.addAll(data)
        if (isMore) this.data.add(null)
        liked = Array(this.data.size) { false }
        notifyDataSetChanged()
    }

    fun addData(data: List<Class>, isMore: Boolean) {
        if (this.data.last() == null) {
            val tmp = this.data.lastIndex
            this.data.removeLast()
            notifyItemRemoved(tmp)
            notifyItemRangeChanged(tmp, itemCount)
        }
        this.isMore = isMore
        val tmp = this.data.count()
        this.data.addAll(data)
        if (isMore) this.data.add(null)
        liked = Array(this.data.size) { false }
        notifyItemInserted(tmp)
    }
}