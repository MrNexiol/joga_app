package com.prograils.joga.ui.classDetails

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.databinding.FragmentClassDetailsBinding

class ClassDetailsFragment : Fragment() {
    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ClassDetailsFragmentArgs by navArgs()
    private var player: SimpleExoPlayer? = null
    private var liked: Boolean = false
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
                initializePlayer(it.videoUrl)
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                    binding.className!!.text = it.title
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
                binding.playButton!!.visibility = View.INVISIBLE
                binding.videoView.visibility = View.VISIBLE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializePlayer(videoUrl: String){
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.videoView.player = player
        val mediaItem = MediaItem.fromUri(videoUrl)
        player!!.setMediaItem(mediaItem)
        player!!.playWhenReady = viewModel.playWhenReady
        player!!.seekTo(viewModel.currentWindow, viewModel.playbackPosition)
        player!!.prepare()
    }
}