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
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
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
        viewModel.castContext = CastContext.getSharedInstance(requireActivity())
        viewModel.navController = findNavController()

        if (args.classIds != null) {
            val classId = args.classIds!!.indexOf(args.classId)
            if (args.classIds!!.size > classId + 1) {
                viewModel.nextClassId = args.classIds!![classId + 1]
            }
        }

        viewModel.classWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
                viewModel.classTitle = resource.data!!.title
                viewModel.initializePlayers(resource.data.videoUrl, requireContext())
                binding.videoView.player = if (viewModel.playRemote) {
                    viewModel.remotePlayer
                } else {
                    viewModel.localPlayer
                }
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    binding.className!!.text = resource.data.title
                    @Suppress("SENSELESS_COMPARISON")
                    if (resource.data.userLike.classId != null) {
                        liked = true
                        binding.likeButton!!.isSelected = liked
                    }
                    binding.likeButton!!.setOnClickListener {
                        liked = !liked
                        viewModel.toggleClassLike()
                        binding.likeButton!!.isSelected = liked
                    }
                    Glide.with(this)
                            .load(resource.data.thumbnailUrl)
                            .fallback(R.drawable.placeholder_image)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(binding.classThumbnail!!)
                    binding.classTitleDuration!!.text = getString(R.string.title_duration, resource.data.title, resource.data.duration)
                    binding.classDescription!!.text = resource.data.description
                    Glide.with(this).load(resource.data.instructor.avatar_url).fallback(R.drawable.trainer_placeholder_icon).into(binding.classInstructorAvatar!!)
                    binding.classInstructorName!!.text = resource.data.instructor.name
                    binding.classInstructorRoot!!.setOnClickListener {
                        val action = ClassDetailsFragmentDirections.actionClassDetailsFragmentToTrainerDetailFragment(resource.data.instructor.id)
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
            CastButtonFactory.setUpMediaRouteButton(requireContext(), binding.castButton)
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
            if (viewModel.isPlaying) showVideoControls()
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
        viewModel.showVideo()
    }

    private fun showVideoControls() {
        binding.playButton?.visibility = View.INVISIBLE
        binding.classThumbnail?.visibility = View.INVISIBLE
        binding.videoView.visibility = View.VISIBLE
    }
}