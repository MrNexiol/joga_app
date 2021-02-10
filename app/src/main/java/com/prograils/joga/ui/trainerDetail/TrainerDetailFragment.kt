package com.prograils.joga.ui.trainerDetail

import android.content.Context
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
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.databinding.FragmentTrainerDetailBinding

class TrainerDetailFragment : Fragment() {
    private var _binding: FragmentTrainerDetailBinding? = null
    private val binding get() = _binding!!
    private val args: TrainerDetailFragmentArgs by navArgs()
    private var player: SimpleExoPlayer? = null
    private lateinit var viewModelFactory: TrainerDetailViewModelFactory
    private lateinit var viewModel: TrainerDetailViewModel
    private var videoUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainerDetailBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)
        viewModelFactory = TrainerDetailViewModelFactory(appContainer.repository, args.trainerId, token!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrainerDetailViewModel::class.java)

        val recyclerView = binding.instructorClassesRecyclerView
        val adapter = TrainerDetailAdapter(listOf(), appContainer.repository, token)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.instructorWrapper.getData().observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                Glide.with(this).load(it.avatar_url).fallback(R.drawable.trainer_placeholder_icon).into(binding.instructorAvatar)
                binding.instructorNameTextView.text = it.name
                initializePlayer(it.videoUrl)
                if (viewModel.isPlaying) {
                    showVideo()
                }
            }
        })
        viewModel.instructorClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                if (it.isEmpty()){
                    binding.noClassesTextView.visibility = View.VISIBLE
                } else {
                    adapter.setData(it)
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.trainerPlayButton.setOnClickListener {
            showVideo()
        }
        binding.trainerDetailsBottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    findNavController().navigate(R.id.action_global_homeFragment)
                    true
                }
                R.id.navigation_classes -> {
                    findNavController().navigate(R.id.action_global_classesFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer(videoUrl)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer(videoUrl)
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showVideo() {
        binding.trainerPlayButton.visibility = View.INVISIBLE
        binding.trainerVideo.visibility = View.VISIBLE
        viewModel.isPlaying = true
        player!!.prepare()
    }

    private fun initializePlayer(videoUrl: String) {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.trainerVideo.player = player
        val mediaItem = MediaItem.fromUri(videoUrl)
        player!!.setMediaItem(mediaItem)
        player!!.playWhenReady = viewModel.playWhenReady
        player!!.seekTo(viewModel.currentWindow, viewModel.playbackPosition)
    }

    private fun releasePlayer() {
        if (player != null){
            viewModel.playWhenReady = player!!.playWhenReady
            viewModel.playbackPosition = player!!.currentPosition
            viewModel.currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }
}