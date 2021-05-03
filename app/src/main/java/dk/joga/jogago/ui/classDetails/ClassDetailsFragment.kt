package dk.joga.jogago.ui.classDetails

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.gms.cast.framework.CastContext
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentClassDetailsBinding
import dk.joga.jogago.ui.MainActivity

class ClassDetailsFragment : Fragment() {
    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ClassDetailsFragmentArgs by navArgs()
    private lateinit var viewModel: ClassDetailsViewModel
    private lateinit var viewModelFactory: ClassDetailsViewModelFactory

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater, container, false)
        viewModelFactory = ClassDetailsViewModelFactory(args.classId, requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClassDetailsViewModel::class.java)

        if (args.classIds != null) {
            val classId = args.classIds!!.indexOf(args.classId)
            if (args.classIds!!.size > classId + 1) {
                viewModel.nextClassId = args.classIds!![classId + 1]
            }
        }

        viewModel.classWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
                (requireActivity() as MainActivity).changeScreenTitle(resource.data!!.title)
                (requireActivity() as MainActivity).setClassId(resource.data.id)
                @Suppress("SENSELESS_COMPARISON")
                if (resource.data.userLike.classId != null) {
                    (requireActivity() as MainActivity).setLikeIcon(true)
                }
                viewModel.initializePlayerManager(binding.videoView, CastContext.getSharedInstance(requireActivity()), resource.data.videoUrl, resource.data.title)
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
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
            if (viewModel.isPlaying() || viewModel.startedVideo) showVideoControls()
            binding.playButton!!.setOnClickListener {
                playVideo()
            }
        }

        val fullscreen: ImageView = view.findViewById(R.id.exo_fullscreen)
        fullscreen.setOnClickListener {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                if (Settings.System.getInt(requireActivity().contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                    // Rotation ON
                    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.wasPlayingDuringStop) {
            viewModel.showVideo()
            viewModel.wasPlayingDuringStop = false
        }
        AppContainer.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Class details")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ClassDetailsFragment")
        }
    }

    override fun onStop() {
        super.onStop()
        if (viewModel.isPlaying()) {
            viewModel.wasPlayingDuringStop = true
        }
        viewModel.stopVideo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).setLikeIcon(false)
        (requireActivity() as MainActivity).setClassId("")
        _binding = null
    }

    private fun playVideo() {
        showVideoControls()
        viewModel.showVideo()
        viewModel.startedVideo = true
    }

    private fun showVideoControls() {
        binding.playButton?.visibility = View.INVISIBLE
        binding.classThumbnail?.visibility = View.INVISIBLE
        binding.videoView.visibility = View.VISIBLE
    }
}