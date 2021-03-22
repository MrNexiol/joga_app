package dk.joga.jogago.ui.classDetails

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import dk.joga.jogago.JoGaApplication
import dk.joga.jogago.R
import dk.joga.jogago.databinding.FragmentClassDetailsBinding

class ClassDetailsFragment : Fragment() {
    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ClassDetailsFragmentArgs by navArgs()
    private var player: SimpleExoPlayer? = null
    private var liked: Boolean = false
    private var videoUrl = ""
    private var classTitle = ""
    private var nextClassId: String? = null
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

        if (args.classIds != null) {
            val classId = args.classIds!!.indexOf(args.classId)
            if (args.classIds!!.size > classId + 1) {
                nextClassId = args.classIds!![classId + 1]
            }
        }

        viewModel.classWrapper.getData().observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                videoUrl = it.videoUrl
                classTitle = it.title
                initializePlayer(it.videoUrl)
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    binding.className!!.text = it.title
                    @Suppress("SENSELESS_COMPARISON")
                    if (it.userLike.classId != null) {
                        liked = true
                        binding.likeButton!!.setImageResource(R.drawable.heart_liked_icon)
                    }
                    binding.likeButton!!.setOnClickListener {
                        if (liked) {
                            liked = false
                            viewModel.toggleClassLike()
                            binding.likeButton!!.setImageResource(R.drawable.heart_not_liked)
                        } else {
                            liked = true
                            viewModel.toggleClassLike()
                            binding.likeButton!!.setImageResource(R.drawable.heart_liked_icon)
                        }
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
            binding.playButton!!.setOnClickListener {
                showVideo()
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
        binding.classThumbnail?.visibility = View.INVISIBLE
        binding.videoView.visibility = View.VISIBLE
        viewModel.isPlaying = true
        player!!.prepare()
        player!!.play()
        player!!.createMessage { _: Int, _: Any? ->
                    viewModel.markAsWatched()
                }
                .setPosition(0,20000)
                .send()
        player!!.addListener(object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    viewModel.markAsWatched()
                    if (nextClassId != null) {
                        val action = ClassDetailsFragmentDirections.actionClassDetailsFragmentSelf(nextClassId!!, args.classIds)
                        findNavController().navigate(action)
                    }
                }
            }
        })
    }

    private fun initializePlayer(videoUrl: String){
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        val hlsMediaSource: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(videoUrl))
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.videoView.player = player
        player!!.setMediaSource(hlsMediaSource)
        player!!.playWhenReady = viewModel.playWhenReady
        player!!.seekTo(viewModel.currentWindow, viewModel.playbackPosition)
        if (viewModel.isPlaying) {
            showVideo()
        }
    }

    private fun releasePlayer(){
        if (player != null){
            viewModel.playWhenReady = player!!.playWhenReady
            viewModel.playbackPosition = player!!.currentPosition
            viewModel.currentWindow = player!!.currentWindowIndex
            player!!.pause()
            player!!.release()
            player = null
        }
    }
}