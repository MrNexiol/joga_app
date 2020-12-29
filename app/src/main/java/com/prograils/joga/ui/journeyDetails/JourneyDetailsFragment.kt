package com.prograils.joga.ui.journeyDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.databinding.FragmentJourneyDetailsBinding

class JourneyDetailsFragment : Fragment() {
    private var _binding: FragmentJourneyDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: JourneyDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJourneyDetailsBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        val journeyViewModel: JourneyViewModel by viewModels { JourneyViewModelFactory(appContainer.repository, args.journeyId) }

        val recyclerView = binding.journeysRecyclerView
        val adapter = JourneyDetailsAdapter(listOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        journeyViewModel.journey.observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                val firstClass = it.classes.first()
                val otherClasses = it.classes - firstClass
                binding.journeyTitle.text = it.name
                Glide.with(this).load(firstClass.thumbnailUrl).into(binding.firstClassThumbnail)
                binding.firstClassNameTextView.text = firstClass.title
                binding.firstClassInstructorNameTextView.text = firstClass.instructor.name
                binding.firstClassMinTextView.text = getString(R.string.min, firstClass.duration)
                binding.firstClassDescription.text = firstClass.description
                adapter.setData(otherClasses)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}