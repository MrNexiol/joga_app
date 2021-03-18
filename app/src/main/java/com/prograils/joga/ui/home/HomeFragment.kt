package com.prograils.joga.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        appContainer = (activity?.application as JoGaApplication).appContainer
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)
        viewModelFactory = HomeViewModelFactory(appContainer.repository, token!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        setNewClassesRecyclerView()
        setLikedClassesRecyclerView()
        setJourneysRecyclerView()
        setInstructorRecyclerView()

        viewModel.newClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success){
                newClassAdapter.setData(resource.data!!)
            } else if (resource.status == Status.Empty){
                newClassAdapter.setData(listOf())
            }
        })
        viewModel.likedClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success){
                likedClassAdapter.setData(resource.data!!.take(3))
                binding.nothingLikedTextView.visibility = View.INVISIBLE
            } else {
                likedClassAdapter.setData(listOf())
                binding.nothingLikedTextView.visibility = View.VISIBLE
            }
        })
        viewModel.journeysWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success){
                journeyAdapter.setData(resource.data!!)
                journeySectionVisibility(true)
            } else if (resource.status == Status.Empty){
                journeyAdapter.setData(listOf())
                journeySectionVisibility(false)
            }
        })
        viewModel.instructorsWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success){
                instructorAdapter.setData(resource.data!!)
            } else if (resource.status == Status.Empty){
                instructorAdapter.setData(listOf())
            }
        })
        viewModel.dailyClassWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
                todaysPickVisibility(true)
                dailyClassId = resource.data!!.id
                Glide.with(this)
                    .load(resource.data.thumbnailUrl)
                    .fallback(R.drawable.placeholder_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.todaysPickThumbnail)
                binding.todayPickName.text = resource.data.title
                binding.todayPickTrainerNameTextView.text = getString(R.string.with, resource.data.instructor.name)
                binding.todayPickMinTextView.text = getString(R.string.min, resource.data.duration)
            } else {
                todaysPickVisibility(false)
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
                    binding.homeScrollView.smoothScrollTo(0,0)
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

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
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

    private fun journeySectionVisibility(visibility: Boolean){
        val showing = if (visibility) View.VISIBLE else View.GONE
        binding.betweenLikedAndJourneysDivider.visibility = showing
        binding.journeysIconHome.visibility = showing
        binding.journeysTextView.visibility = showing
        binding.seeJourneysButton.visibility = showing
        binding.journeyRecyclerView.visibility = showing
    }

    private fun todaysPickVisibility(visibility: Boolean){
        val showing = if (visibility) View.VISIBLE else View.GONE
        binding.todayPickIcon.visibility = showing
        binding.todayPickTextView.visibility = showing
        binding.todaysPickRoot.visibility = showing
        binding.secondDivider.visibility = showing
    }
}