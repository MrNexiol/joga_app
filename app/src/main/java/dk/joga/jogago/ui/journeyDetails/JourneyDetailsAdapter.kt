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
import dk.joga.jogago.databinding.JourneyClassesRecyclerViewItemBinding

class JourneyDetailsAdapter : RecyclerView.Adapter<JourneyDetailsAdapter.ViewHolder>() {

    private var data: List<Class> = listOf()

    class ViewHolder(
            val binding: JourneyClassesRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = JourneyClassesRecyclerViewItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
                .load(data[position].thumbnailUrl)
                .fallback(R.drawable.placeholder_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.journeyClassThumbnail)
        holder.binding.journeyClassName.text = data[position].title
        holder.binding.journeyClassFocus.text = data[position].focus
        holder.binding.journeyClassMin.text = holder.itemView.context.getString(R.string.min, data[position].duration)
        holder.binding.journeyClassInstructorName.text = data[position].instructor.name
        if (!data[position].watchable){
            val color = ContextCompat.getColor(holder.itemView.context, R.color.secondary_text)
            holder.binding.journeyClassThumbnail.alpha = 0.3F
            holder.binding.journeyClassName.setTextColor(color)
            holder.binding.journeyClassFocus.setTextColor(color)
            holder.binding.journeyClassMin.setTextColor(color)
            holder.binding.journeyClassInstructorName.setTextColor(color)
        } else {
            holder.binding.root.setOnClickListener {
                val idList = mutableListOf<String>()
                data.forEachIndexed { i, c ->
                    idList.add(i, c.id)
                }
                val action = JourneyDetailsFragmentDirections.actionJourneyDetailsFragmentToClassDetailsFragment(data[position].id, idList.toTypedArray())
                holder.itemView.findNavController().navigate(action)
            }
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