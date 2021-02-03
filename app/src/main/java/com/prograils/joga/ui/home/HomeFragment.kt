package com.prograils.joga.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.prograils.joga.AppContainer
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.api.Status
import com.prograils.joga.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var appContainer: AppContainer
    private lateinit var newClassRecyclerView: RecyclerView
    private lateinit var newClassAdapter: NewClassesAdapter
    private lateinit var likedClassRecyclerView: RecyclerView
    private lateinit var likedClassAdapter: LikedClassAdapter
    private lateinit var instructorRecyclerView: RecyclerView
    private lateinit var instructorAdapter: InstructorsAdapter
    private lateinit var journeyRecyclerView: RecyclerView
    private lateinit var journeyAdapter: JourneysAdapter
    private lateinit var dailyClassId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        appContainer = (activity?.application as JoGaApplication).appContainer
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)
        val homeViewModel: HomeViewModel by viewModels { HomeViewModelFactory(appContainer.repository, token!!) }

        setNewClassesRecyclerView()
        setLikedClassesRecyclerView()
        setJourneysRecyclerView()
        setInstructorRecyclerView()

        homeViewModel.newClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success){
                newClassAdapter.setData(resource.data!!)
            }
        })

        homeViewModel.likedClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success){
                likedClassAdapter.setData(resource.data!!.take(3))
            } else {
                binding.nothingLikedTextView.visibility = View.VISIBLE
            }
        })
        homeViewModel.journeysWrapper.getData().observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                journeyAdapter.setData(it.take(3))
            }
        })
        homeViewModel.instructorsWrapper.getData().observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                instructorAdapter.setData(it)
            }
        })
        homeViewModel.dailyClassWrapper.getData().observe(viewLifecycleOwner, { resource ->
            resource?.data?.let {
                dailyClassId = it.id
                Glide.with(this)
                        .load(it.thumbnailUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.todaysPickThumbnail)
                binding.todayPickName.text = it.title
                binding.todayPickTrainerNameTextView.text = getString(R.string.with, it.instructor.name)
                binding.todayPickMinTextView.text = getString(R.string.min, it.duration)
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appContainer = (activity?.application as JoGaApplication).appContainer
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)
        val homeViewModel: HomeViewModel by viewModels { HomeViewModelFactory(appContainer.repository, token!!) }

        binding.homeRefreshLayout.setOnRefreshListener {
            homeViewModel.refreshData()
            binding.homeRefreshLayout.isRefreshing = false
        }
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
            val action = HomeFragmentDirections.actionHomeFragmentToLogoutFragment()
            findNavController().navigate(action)
        }
        binding.navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    binding.homeScrollView.scrollTo(0,0)
                    true
                }
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

    private fun setNewClassesRecyclerView(){
        newClassRecyclerView = binding.newClassesRecyclerView
        newClassAdapter = NewClassesAdapter(listOf())
        newClassRecyclerView.adapter = newClassAdapter
        newClassRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setLikedClassesRecyclerView(){
        likedClassRecyclerView = binding.likedRecyclerView
        likedClassAdapter = LikedClassAdapter(listOf())
        likedClassRecyclerView.adapter = likedClassAdapter
        likedClassRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setJourneysRecyclerView(){
        journeyRecyclerView = binding.journeyRecyclerView
        journeyAdapter = JourneysAdapter(listOf())
        journeyRecyclerView.adapter = journeyAdapter
        journeyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setInstructorRecyclerView(){
        instructorRecyclerView = binding.instructorRecyclerView
        instructorAdapter = InstructorsAdapter(listOf())
        instructorRecyclerView.adapter = instructorAdapter
        instructorRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}