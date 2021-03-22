package dk.joga.jogago.ui.journeys

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dk.joga.jogago.R
import dk.joga.jogago.api.Journey
import dk.joga.jogago.databinding.JourneysRecyclerViewItemBinding

class JourneysAdapter(private var data: List<Journey>) : RecyclerView.Adapter<JourneysAdapter.ViewHolder>() {

    class ViewHolder(
            val binding: JourneysRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = JourneysRecyclerViewItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
                .load(data[position].coverUrl)
                .fallback(R.drawable.placeholder_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.journeyThumbnailImage)
        holder.binding.journeyNameTextView.text = data[position].name
        holder.binding.root.setOnClickListener {
            val action = JourneysFragmentDirections.actionJourneysFragmentToJourneyDetailsFragment(data[position].id)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<Journey>){
        this.data = data
        notifyDataSetChanged()
    }
}