package dk.joga.jogago.ui.trainerDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentTrainerDetailBinding
import dk.joga.jogago.ui.MainActivity

class TrainerDetailFragment : Fragment() {
    private var _binding: FragmentTrainerDetailBinding? = null
    private val binding get() = _binding!!
    private val args: TrainerDetailFragmentArgs by navArgs()
    private var player: SimpleExoPlayer? = null
    private val adapter = TrainerDetailAdapter()
    private lateinit var viewModelFactory: TrainerDetailViewModelFactory
    private lateinit var viewModel: TrainerDetailViewModel
    private var videoUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainerDetailBinding.inflate(inflater, container, false)
        viewModelFactory = TrainerDetailViewModelFactory(args.trainerId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrainerDetailViewModel::class.java)

        val recyclerView = binding.instructorClassesRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.instructorWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
                (requireActivity() as MainActivity).changeScreenTitle(resource.data!!.name)
                Glide.with(this).load(resource.data.avatar_url).fallback(R.drawable.placeholder_image).into(binding.trainerThumbnail)
                @Suppress("SENSELESS_COMPARISON")
                if (resource.data.videoUrl != "" && resource.data.videoUrl != null) {
                    binding.trainerVideoRoot.visibility = View.VISIBLE
                    binding.trainerThumbnailAndListDivider.visibility = View.VISIBLE
                    binding.trainerVideoTitle.visibility = View.VISIBLE
                    binding.trainerVideoDuration.visibility = View.VISIBLE
                    binding.trainerVideoTitle.text = resource.data.videoTitle
                    binding.trainerVideoDuration.text = getString(R.string.min, resource.data.videoDuration)
                    initializePlayer(resource.data.videoUrl)
                    if (viewModel.isPlaying) {
                        showVideo()
                    }
                } else {
                    binding.trainerVideoRoot.visibility = View.GONE
                    binding.trainerThumbnailAndListDivider.visibility = View.GONE
                    binding.trainerVideoTitle.visibility = View.GONE
                    binding.trainerVideoDuration.visibility = View.GONE
                }
            }
        })
        viewModel.instructorClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
                adapter.setData(resource.data!!)
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.trainerPlayButton.setOnClickListener {
            showVideo()
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