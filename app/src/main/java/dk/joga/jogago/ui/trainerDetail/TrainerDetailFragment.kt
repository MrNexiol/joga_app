package dk.joga.jogago.ui.trainerDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.cast.framework.CastContext
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentTrainerDetailBinding
import dk.joga.jogago.ui.MainActivity

class TrainerDetailFragment : Fragment() {
    private var _binding: FragmentTrainerDetailBinding? = null
    private val binding get() = _binding!!
    private val args: TrainerDetailFragmentArgs by navArgs()
    private val adapter = TrainerDetailAdapter()
    private var itemsCount = 0
    private var videoShown = false
    private lateinit var viewModelFactory: TrainerDetailViewModelFactory
    private lateinit var viewModel: TrainerDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainerDetailBinding.inflate(inflater, container, false)
        viewModelFactory = TrainerDetailViewModelFactory(args.trainerId, requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrainerDetailViewModel::class.java)

        viewModel.instructorWrapper.getData().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.Success -> {
                    (requireActivity() as MainActivity).changeScreenTitle(resource.data!!.name)
                    @Suppress("SENSELESS_COMPARISON")
                    if (resource.data.videoUrl != "" && resource.data.videoUrl != null) {
                        Glide.with(this).load(resource.data.avatar_url).fallback(R.drawable.placeholder_image).into(binding.trainerThumbnail)
                        viewModel.initializePlayerManager(binding.trainerVideo, CastContext.getSharedInstance(requireActivity()), resource.data.videoUrl, resource.data.videoTitle)
                        if (!videoShown) {
                            binding.trainerMotionLayout.transitionToStart()
                            videoShown = true
                        }
                        binding.trainerVideoTitle.text = resource.data.videoTitle
                        binding.trainerVideoDuration.text = getString(R.string.min, resource.data.videoDuration)
                    } else {
                        binding.trainerMotionLayout.setTransition(R.id.collapsed, R.id.collapsed)
                        binding.trainerMotionLayout.getTransition(R.id.video_transition).setEnable(false)
                    }
                }
                Status.Empty -> {}
                Status.Unauthorized -> (activity as MainActivity).logout()
                Status.SubscriptionEnded -> (activity as MainActivity).subscriptionError()
                else -> Toast.makeText(context, R.string.connection_error, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.instructorClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.Success -> {
                    if (itemsCount + resource.data!!.size == resource.totalCount) {
                        viewModel.isMore = false
                    }
                    if (viewModel.isLoading) {
                        adapter.addData(resource.data, viewModel.isMore)
                        itemsCount += resource.data.count()
                        viewModel.isLoading = false
                    } else {
                        adapter.setData(resource.data, viewModel.isMore)
                        itemsCount = resource.data.count()
                    }
                }
                Status.Empty -> {}
                Status.Unauthorized -> (activity as MainActivity).logout()
                Status.SubscriptionEnded -> (activity as MainActivity).subscriptionError()
                else -> Toast.makeText(context, R.string.connection_error, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.startedVideo) showVideoControls()
        binding.trainerPlayButton.setOnClickListener {
            startVideo()
        }

        val fullscreen: ImageView = view.findViewById(R.id.exo_fullscreen)
        val playButton: ImageView = view.findViewById(R.id.exo_play)
        val pauseButton: ImageView = view.findViewById(R.id.exo_pause)
        val durationTextView: TextView = view.findViewById(R.id.exo_duration)
        fullscreen.visibility = View.GONE
        durationTextView.setPaddingRelative(0,0,resources.getDimensionPixelSize(R.dimen.margin_small),0)

        binding.root.setOnRefreshListener {
            viewModel.resetData()
            binding.root.isRefreshing = false
        }
        pauseButton.setOnClickListener {
            viewModel.stopVideo()
            (activity as MainActivity).allowScreenLocking()
        }
        playButton.setOnClickListener {
            viewModel.playVideo()
            (activity as MainActivity).preventScreenLocking()
        }

        val recyclerView = binding.instructorClassesRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastVisibleItemPosition() >= itemsCount - 1 && viewModel.isMore) {
                    viewModel.changePageNumber()
                } else if (layoutManager.findFirstCompletelyVisibleItemPosition() >= 1) {
                    binding.trainerMotionLayout.progress = 1f
                }
                binding.root.isEnabled = layoutManager.findLastVisibleItemPosition() <= 1
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.wasPlayingOnStop) {
            startVideo()
            viewModel.wasPlayingOnStop = false
        }
        if (viewModel.isPlaying()) {
            (activity as MainActivity).preventScreenLocking()
        } else {
            (activity as MainActivity).allowScreenLocking()
        }
        AppContainer.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "trainer_classes")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "TrainerDetailsFragment")
        }
    }

    override fun onStop() {
        super.onStop()
        if (viewModel.isPlaying()) {
            viewModel.wasPlayingOnStop = true
            viewModel.stopVideo()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).allowScreenLocking()
        _binding = null
    }

    private fun showVideoControls() {
        binding.trainerPlayButton.visibility = View.INVISIBLE
        binding.trainerVideo.visibility = View.VISIBLE
    }

    private fun startVideo() {
        showVideoControls()
        viewModel.playVideo()
        viewModel.startedVideo = true
        (activity as MainActivity).preventScreenLocking()
    }
}