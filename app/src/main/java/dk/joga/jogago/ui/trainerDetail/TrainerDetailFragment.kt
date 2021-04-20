package dk.joga.jogago.ui.trainerDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.cast.framework.CastContext
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
    private var isMore = true
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

        findNavController().addOnDestinationChangedListener { _, _, _ ->
            viewModel.pauseVideo()
        }

        viewModel.instructorWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
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
                    if (viewModel.isPlaying()) {
                        showVideo()
                    }
                } else {
                    binding.trainerMotionLayout.setTransition(R.id.collapsed, R.id.collapsed)
                    binding.trainerMotionLayout.getTransition(R.id.video_transition).setEnable(false)
                }
            }
        })
        viewModel.instructorClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
                if (viewModel.isLoading) {
                    adapter.addData(resource.data!!)
                    itemsCount += resource.data.count()
                    viewModel.isLoading = false
                } else {
                    adapter.setData(resource.data!!)
                    itemsCount = resource.data.count()
                }
                isMore = itemsCount != resource.totalCount
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.trainerPlayButton.setOnClickListener {
            showVideo()
        }

        val fullscreen: ImageView = view.findViewById(R.id.exo_fullscreen)
        val durationTextView: TextView = view.findViewById(R.id.exo_duration)
        fullscreen.visibility = View.GONE
        durationTextView.setPaddingRelative(0,0,resources.getDimensionPixelSize(R.dimen.margin_small),0)

        binding.root.setOnRefreshListener {
            viewModel.resetData()
            binding.root.isRefreshing = false
        }

        val recyclerView = binding.instructorClassesRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastVisibleItemPosition() >= itemsCount - 1 && isMore) {
                    viewModel.changePageNumber()
                } else if (layoutManager.findFirstCompletelyVisibleItemPosition() >= 1) {
                    binding.trainerMotionLayout.progress = 1f
                }
                binding.root.isEnabled = layoutManager.findLastVisibleItemPosition() <= 1
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showVideo() {
        binding.trainerPlayButton.visibility = View.INVISIBLE
        binding.trainerVideo.visibility = View.VISIBLE
        viewModel.showVideo()
    }
}