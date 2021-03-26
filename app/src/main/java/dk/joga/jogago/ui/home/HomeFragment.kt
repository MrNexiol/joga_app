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
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var newClassRecyclerView: RecyclerView
    private lateinit var newClassAdapter: NewClassesAdapter
    private lateinit var likedClassRecyclerView: RecyclerView
    private lateinit var likedClassAdapter: LikedClassAdapter
    private lateinit var instructorRecyclerView: RecyclerView
    private lateinit var instructorAdapter: InstructorsAdapter
    private lateinit var journeyRecyclerView: RecyclerView
    private lateinit var journeyAdapter: JourneysAdapter
    private lateinit var dailyClassId: String
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(getString(R.string.saved_token_key), null)
        if (token == null){
            val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            findNavController().navigate(action)
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
            if (resource.status == Status.Success){
                newClassAdapter.setData(resource.data!!)
            } else if (resource.status == Status.Empty){
                newClassAdapter.setData(listOf())
            }
        })
        viewModel.likedClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success){
                likedClassAdapter.setData(resource.data!!.take(3))
                likedClassesSectionVisibility(true)
            } else {
                likedClassAdapter.setData(listOf())
                likedClassesSectionVisibility(false)
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
                        .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.card_radius)))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.todaysPickThumbnail)
                binding.todayPickName.text = resource.data.title
                binding.todayPickTrainerNameTextView.text = getString(R.string.with, resource.data.instructor.name)
                binding.todayPickMinTextView.text = getString(R.string.min, resource.data.duration)
                binding.todayPickCategory.text = resource.data.categories.joinToString()
            } else {
                todaysPickVisibility(false)
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