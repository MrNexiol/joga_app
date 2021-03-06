package dk.joga.jogago.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dk.joga.jogago.R
import dk.joga.jogago.api.Instructor
import dk.joga.jogago.databinding.HomeInstructorsRecyclerViewItemBinding

class InstructorsAdapter : RecyclerView.Adapter<InstructorsAdapter.ViewHolder>() {

    private var data: List<Instructor> = listOf()

    class ViewHolder(
            val binding: HomeInstructorsRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeInstructorsRecyclerViewItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmp = data[position].name.split(" ")
        var splitName = ""
        for (word in tmp){
            splitName += "$word\n"
        }
        Glide.with(holder.itemView)
                .load(data[position].avatar_url)
                .fallback(R.drawable.trainer_placeholder_icon)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.trainerAvatar)
        holder.binding.trainerName.text = splitName
        holder.binding.root.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToTrainerDetailFragment(data[position].id)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<Instructor>){
        this.data = data
        notifyDataSetChanged()
    }
}