package dk.joga.jogago.ui.trainerDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
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
            if (resource.status == Status.Success) {
                (requireActivity() as MainActivity).changeScreenTitle(resource.data!!.name)
                @Suppress("SENSELESS_COMPARISON")
                if (resource.data.videoUrl != "" && resource.data.videoUrl != null) {
                    Glide.with(this).load(resource.data.avatar_url).fallback(R.drawable.placeholder_image).into(binding.trainerThumbnail)
                    viewModel.initializePlayerManager(binding.trainerVideo, CastContext.getSharedInstance(requireActivity()), resource.data.videoUrl, resource.data.videoTitle)
                    if (!videoShown) {
                        binding.root.transitionToStart()
                        videoShown = true
                    }
                    binding.trainerVideoTitle.text = resource.data.videoTitle
                    binding.trainerVideoDuration.text = getString(R.string.min, resource.data.videoDuration)
                    if (viewModel.isPlaying()) {
                        showVideo()
                    }
                } else {
                    binding.root.setTransition(R.id.collapsed, R.id.collapsed)
                    binding.root.getTransition(R.id.video_transition).setEnable(false)
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
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.trainerPlayButton.setOnClickListener {
            showVideo()
        }

        val recyclerView = binding.instructorClassesRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastVisibleItemPosition() >= itemsCount - 1) {
                    viewModel.changePageNumber()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
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