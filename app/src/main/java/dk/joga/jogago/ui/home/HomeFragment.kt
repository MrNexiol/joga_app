package dk.joga.jogago.ui.home

import android.content.Context
import android.content.SharedPreferences
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentHomeBinding
import dk.joga.jogago.ui.MainActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val newClassAdapter = NewClassesAdapter()
    private val likedClassAdapter = LikedClassAdapter()
    private val instructorAdapter = InstructorsAdapter()
    private val journeyAdapter = JourneysAdapter()
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var newClassRecyclerView: RecyclerView
    private lateinit var likedClassRecyclerView: RecyclerView
    private lateinit var instructorRecyclerView: RecyclerView
    private lateinit var journeyRecyclerView: RecyclerView
    private lateinit var dailyClassId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(getString(R.string.saved_token_key), null)
        if (token == null){
            findNavController().navigate(R.id.action_global_loginFragment)
        } else {
            AppContainer.firebaseAnalytics.setUserId(sharedPreferences.getString(getString(R.string.saved_user_id), ""))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setNewClassesRecyclerView()
        setLikedClassesRecyclerView()
        setJourneysRecyclerView()
        setInstructorRecyclerView()

        viewModel.newClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.Success -> newClassAdapter.setData(resource.data!!)
                Status.Empty -> newClassAdapter.setData(listOf())
                Status.SubscriptionEnded -> (activity as MainActivity).subscriptionError()
                else -> {}
            }
        })
        viewModel.likedClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.Success -> {
                    likedClassAdapter.setData(resource.data!!.take(3))
                    likedClassesSectionVisibility(true)
                }
                Status.Empty -> {
                    likedClassAdapter.setData(listOf())
                    likedClassesSectionVisibility(false)
                }
                Status.SubscriptionEnded -> (activity as MainActivity).subscriptionError()
                else -> { }
            }
        })
        viewModel.journeysWrapper.getData().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.Success -> {
                    journeyAdapter.setData(resource.data!!.take(3))
                    journeySectionVisibility(true)
                }
                Status.Empty -> {
                    journeyAdapter.setData(listOf())
                    journeySectionVisibility(false)
                }
                Status.SubscriptionEnded -> (activity as MainActivity).subscriptionError()
                else -> { }
            }
        })
        viewModel.instructorsWrapper.getData().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.Success -> instructorAdapter.setData(resource.data!!)
                Status.Empty -> instructorAdapter.setData(listOf())
                Status.SubscriptionEnded -> (activity as MainActivity).subscriptionError()
                else -> { }
            }
        })
        viewModel.dailyClassWrapper.getData().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.Success -> {
                    todaysPickVisibility(true)
                    dailyClassId = resource.data!!.id
                    Glide.with(this)
                        .load(resource.data.thumbnailUrl)
                        .fallback(R.drawable.placeholder_image)
                        .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.card_radius)))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.todaysPickThumbnail)
                    binding.todayPickWatchedIcon.visibility = if (resource.data.watched) View.VISIBLE else View.GONE
                    binding.todayPickName.text = resource.data.title
                    binding.todayPickTrainerNameTextView.text = getString(R.string.with, resource.data.instructor.name)
                    binding.todayPickMinTextView.text = getString(R.string.min, resource.data.duration)
                    binding.todayPickCategory.text = resource.data.categories.joinToString()
                }
                Status.Empty -> todaysPickVisibility(false)
                Status.SubscriptionEnded -> (activity as MainActivity).subscriptionError()
                else -> {
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData()
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
        AppContainer.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "home")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "HomeFragment")
        }
    }

    private fun setNewClassesRecyclerView(){
        newClassRecyclerView = binding.newClassesRecyclerView
        newClassRecyclerView.adapter = newClassAdapter
        newClassRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setLikedClassesRecyclerView(){
        likedClassRecyclerView = binding.likedRecyclerView
        likedClassRecyclerView.adapter = likedClassAdapter
        likedClassRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setJourneysRecyclerView(){
        journeyRecyclerView = binding.journeyRecyclerView
        journeyRecyclerView.adapter = journeyAdapter
        journeyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setInstructorRecyclerView(){
        instructorRecyclerView = binding.instructorRecyclerView
        instructorRecyclerView.adapter = instructorAdapter
        instructorRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun todaysPickVisibility(visibility: Boolean){
        val showing = if (visibility) View.VISIBLE else View.GONE
        binding.todayPickIcon.visibility = showing
        binding.todayPickTextView.visibility = showing
        binding.todaysPickRoot.visibility = showing
        binding.secondDivider.visibility = showing
    }

    private fun likedClassesSectionVisibility(visibility: Boolean){
        val showing = if (visibility) View.VISIBLE else View.GONE
        binding.betweenNewAndLikedClassesDivider.visibility = showing
        binding.likedIcon.visibility = showing
        binding.likedTextView.visibility = showing
        binding.seeLikedButton.visibility = showing
        binding.likedRecyclerView.visibility = showing
    }

    private fun journeySectionVisibility(visibility: Boolean){
        val showing = if (visibility) View.VISIBLE else View.GONE
        binding.betweenLikedAndJourneysDivider.visibility = showing
        binding.journeysIconHome.visibility = showing
        binding.journeysTextView.visibility = showing
        binding.seeJourneysButton.visibility = showing
        binding.journeyRecyclerView.visibility = showing
    }
}