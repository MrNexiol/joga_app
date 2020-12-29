package com.prograils.joga.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var instructorRecyclerView: RecyclerView
    private lateinit var instructorsAdapter: InstructorsAdapter
    private lateinit var journeyRecyclerView: RecyclerView
    private lateinit var journeysAdapter: JourneysAdapter
    private lateinit var dailyClassId: String

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
        homeViewModel.dailyClass.observe(viewLifecycleOwner, {
            dailyClassId = it.lecture.id
            Glide.with(this).load(it.lecture.thumbnailUrl).into(binding.todaysPickThumbnail)
            binding.todayPickNameTextView.text = it.lecture.title
            binding.todayPickTrainerNameTextView.text = it.lecture.instructor.name
            binding.todayPickMinTextView.text = getString(R.string.min, it.lecture.duration)
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.todaysPickRoot.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToClassDetailsFragment(dailyClassId)
            findNavController().navigate(action)
        }
        binding.seeLikedButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToLikedFragment()
            findNavController().navigate(action)
        }
        binding.seeJourneysButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToJourneysFragment()
            findNavController().navigate(action)
        }
        binding.logoutButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        binding.navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_classes -> {
                    val action = HomeFragmentDirections.actionHomeFragmentToClassesFragment()
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }
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