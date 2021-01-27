package com.prograils.joga.ui.classDetails

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.databinding.FragmentClassDetailsBinding
import java.util.*

class ClassDetailsFragment : Fragment() {
    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ClassDetailsFragmentArgs by navArgs()
    private var player: SimpleExoPlayer? = null
    private var liked: Boolean = false
    private var videoUrl = ""
    private lateinit var viewModel: ClassDetailsViewModel
    private lateinit var viewModelFactory: ClassDetailsViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater, container, false)
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        viewModelFactory = ClassDetailsViewModelFactory(appContainer.repository, token!!, args.classId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClassDetailsViewModel::class.java)

        viewModel.singleClass.observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                videoUrl = it.videoUrl
                initializePlayer(it.videoUrl)
                if (viewModel.isPlaying){
                    showVideo()
                }
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                    binding.className!!.text = it.title
                    @Suppress("SENSELESS_COMPARISON")
                    if (it.userLike.classId != null){
                        liked = true
                        binding.likeButton!!.setImageResource(R.drawable.heart_liked_icon)
                    }
                    binding.likeButton!!.setOnClickListener {
                        if (liked){
                            liked = false
                            viewModel.toggleClassLike()
                            binding.likeButton!!.setImageResource(R.drawable.heart_not_liked)
                        } else {
                            liked = true
                            viewModel.toggleClassLike()
                            binding.likeButton!!.setImageResource(R.drawable.heart_liked_icon)
                        }
                    }
                    Glide.with(this).load(it.thumbnailUrl).into(binding.classThumbnail!!)
                    binding.classTitleDuration!!.text = getString(R.string.title_duration, it.title, it.duration)
                    binding.classDescription!!.text = it.description
                    Glide.with(this).load(it.instructor.avatar_url).into(binding.classInstructorAvatar!!)
                    binding.classInstructorName!!.text = it.instructor.name
                    binding.classInstructorRoot!!.setOnClickListener { _ ->
                        val action = ClassDetailsFragmentDirections.actionClassDetailsFragmentToTrainerDetailFragment(it.instructor.id)
                        findNavController().navigate(action)
                    }
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            binding.playButton!!.setOnClickListener {
                showVideo()
            }

            binding.bottomNavigationClassDetails!!.setOnNavigationItemSelectedListener {
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
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer(videoUrl)
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer(videoUrl)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun showVideo(){
        binding.playButton?.visibility = View.INVISIBLE
        binding.videoView.visibility = View.VISIBLE
        viewModel.isPlaying = true
        player!!.prepare()
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask(){
            override fun run() {
                if (player!!.currentPosition > 20000) {
                    viewModel.markAsWatched()
                }
            }
        }, 0, 5000)
    }

    private fun initializePlayer(videoUrl: String){
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.videoView.player = player
        val mediaItem = MediaItem.fromUri(videoUrl)
        player!!.setMediaItem(mediaItem)
        player!!.playWhenReady = viewModel.playWhenReady
        player!!.seekTo(viewModel.currentWindow, viewModel.playbackPosition)
    }

    private fun releasePlayer(){
        if (player != null){
            viewModel.playWhenReady = player!!.playWhenReady
            viewModel.playbackPosition = player!!.currentPosition
            viewModel.currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }
}