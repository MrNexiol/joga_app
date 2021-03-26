package dk.joga.jogago.ui.journeyDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dk.joga.jogago.JoGaApplication
import dk.joga.jogago.R
import dk.joga.jogago.databinding.FragmentJourneyDetailsBinding

class JourneyDetailsFragment : Fragment() {
    private var _binding: FragmentJourneyDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: JourneyDetailsFragmentArgs by navArgs()
    private lateinit var viewModelFactory: JourneyViewModelFactory
    private lateinit var viewModel: JourneyViewModel
    private lateinit var adapter: JourneyDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJourneyDetailsBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        viewModelFactory = JourneyViewModelFactory(appContainer.repository, args.journeyId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(JourneyViewModel::class.java)

        val recyclerView = binding.journeysRecyclerView
        adapter = JourneyDetailsAdapter(listOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.journeyWrapper.getData().observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                val firstClass = it.classes.first()
                val otherClasses = it.classes - firstClass
                val idList = mutableListOf<String>()
                otherClasses.forEachIndexed { i, c ->
                    idList.add(i, c.id)
                }
                binding.journeyTitle.text = it.name
                Glide.with(this)
                        .load(firstClass.thumbnailUrl)
                        .fallback(R.drawable.placeholder_image)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.firstClassThumbnail)
                binding.firstClassNameTextView.text = firstClass.title
                binding.firstClassInstructorNameTextView.text = getString(R.string.with, firstClass.instructor.name)
                binding.firstClassMinTextView.text = getString(R.string.min, firstClass.duration)
                binding.firstClassDescription.text = firstClass.description
                binding.firstClassRoot.setOnClickListener {
                    val action = JourneyDetailsFragmentDirections.actionJourneyDetailsFragmentToClassDetailsFragment(firstClass.id, idList.toTypedArray())
                    findNavController().navigate(action)
                }
                adapter.setData(otherClasses)
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshData()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}