package com.prograils.joga.ui.journeyDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.databinding.FragmentJourneyDetailsBinding

class JourneyDetailsFragment : Fragment() {
    private var _binding: FragmentJourneyDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: JourneyDetailsFragmentArgs by navArgs()
    private lateinit var journeyViewModel: JourneyViewModel
    private lateinit var journeyClassesRecyclerView: RecyclerView
    private lateinit var journeyDetailsAdapter: JourneyDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJourneyDetailsBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        journeyViewModel = JourneyViewModel(appContainer.repository, args.journeyId)

        journeyClassesRecyclerView = binding.journeysRecyclerView
        journeyDetailsAdapter = JourneyDetailsAdapter(listOf())
        journeyClassesRecyclerView.adapter = journeyDetailsAdapter
        journeyClassesRecyclerView.layoutManager = LinearLayoutManager(context)

        journeyViewModel.journey.observe(viewLifecycleOwner, {
            val firstClass = it.journey.classes.first()
            val otherClasses = it.journey.classes - firstClass
            binding.journeyTitle.text = it.journey.name
            Glide.with(this).load(firstClass.thumbnailUrl).into(binding.firstClassThumbnail)
            binding.firstClassNameTextView.text = firstClass.title
            binding.firstClassInstructorNameTextView.text = firstClass.instructor.name
            binding.firstClassMinTextView.text = getString(R.string.min, firstClass.duration)
            binding.firstClassDescription.text = firstClass.description
            journeyDetailsAdapter.setData(otherClasses)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}