package com.prograils.joga.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prograils.joga.JoGaApplication
import com.prograils.joga.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var instructorRecyclerView: RecyclerView
    private lateinit var instructorsAdapter: InstructorsAdapter
    private lateinit var journeyRecyclerView: RecyclerView
    private lateinit var journeysAdapter: JourneysAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        homeViewModel = HomeViewModel(appContainer.repository)

        setJourneysRecyclerView()
        setInstructorRecyclerView()

        homeViewModel.instructors.observe(viewLifecycleOwner, {
            instructorsAdapter.setData(it.instructors)
        })
        homeViewModel.journeys.observe(viewLifecycleOwner, {
            journeysAdapter.setData(it.journeys)
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setJourneysRecyclerView(){
        journeyRecyclerView = binding.journeyRecyclerView
        journeysAdapter = JourneysAdapter(listOf())
        journeyRecyclerView.adapter = journeysAdapter
        journeyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setInstructorRecyclerView(){
        instructorRecyclerView = binding.instructorRecyclerView
        instructorsAdapter = InstructorsAdapter(listOf())
        instructorRecyclerView.adapter = instructorsAdapter
        instructorRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}