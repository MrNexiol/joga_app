package dk.joga.jogago.ui.classDetails

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dk.joga.jogago.R
import dk.joga.jogago.databinding.FragmentClassDetailsBinding

class ClassDetailsFragment : Fragment() {
    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ClassDetailsFragmentArgs by navArgs()
    private var liked: Boolean = false
    private lateinit var viewModel: ClassDetailsViewModel
    private lateinit var viewModelFactory: ClassDetailsViewModelFactory

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater, container, false)
        viewModelFactory = ClassDetailsViewModelFactory(args.classId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClassDetailsViewModel::class.java)
        viewModel.classIds = args.classIds

        if (args.classIds != null) {
            val classId = args.classIds!!.indexOf(args.classId)
            if (args.classIds!!.size > classId + 1) {
                viewModel.nextClassId = args.classIds!![classId + 1]
            }
        }

        viewModel.classWrapper.getData().observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                viewModel.initializePlayer(it.videoUrl, requireContext())
                binding.videoView.player = viewModel.player
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    binding.className!!.text = it.title
                    @Suppress("SENSELESS_COMPARISON")
                    if (it.userLike.classId != null) {
                        liked = true
                        binding.likeButton!!.isSelected = liked
                    }
                    binding.likeButton!!.setOnClickListener {
                        liked = !liked
                        viewModel.toggleClassLike()
                        binding.likeButton!!.isSelected = liked
                    }
                    Glide.with(this)
                            .load(it.thumbnailUrl)
                            .fallback(R.drawable.placeholder_image)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(binding.classThumbnail!!)
                    binding.classTitleDuration!!.text = getString(R.string.title_duration, it.title, it.duration)
                    binding.classDescription!!.text = it.description
                    Glide.with(this).load(it.instructor.avatar_url).fallback(R.drawable.trainer_placeholder_icon).into(binding.classInstructorAvatar!!)
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
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
            if (viewModel.isPlaying) {
                showVideoControls()
            }
            binding.playButton!!.setOnClickListener {
                playVideo()
            }
        }

        val fullscreen: ImageView = view.findViewById(R.id.exo_fullscreen)
        fullscreen.setOnClickListener {
            if (requireActivity().requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            } else {
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun playVideo() {
        showVideoControls()
        viewModel.isPlaying = true
        viewModel.showVideo(findNavController())
    }

    private fun showVideoControls() {
        binding.playButton?.visibility = View.INVISIBLE
        binding.classThumbnail?.visibility = View.INVISIBLE
        binding.videoView.visibility = View.VISIBLE
    }
}