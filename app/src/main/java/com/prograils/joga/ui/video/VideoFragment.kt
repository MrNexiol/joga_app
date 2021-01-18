package com.prograils.joga.ui.video

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.prograils.joga.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private val args: VideoFragmentArgs by navArgs()
    private var player: SimpleExoPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity?.window?.insetsController?.hide(WindowInsets.Type.statusBars())
            activity?.window?.insetsController?.hide(WindowInsets.Type.navigationBars())
        }
        else {
            @Suppress("DEPRECATION")
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity?.window?.insetsController?.show(WindowInsets.Type.statusBars())
            activity?.window?.insetsController?.show(WindowInsets.Type.navigationBars())
        }
        else {
            @Suppress("DEPRECATION")
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializePlayer(){
        val viewModel: VideoViewModel by viewModels()
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.videoView.player = player
        val mediaItem = MediaItem.fromUri(args.videoUrl)
        player!!.setMediaItem(mediaItem)
        player!!.playWhenReady = viewModel.playWhenReady
        player!!.seekTo(viewModel.currentWindow, viewModel.playbackPosition)
        player!!.prepare()
    }

    private fun releasePlayer(){
        val viewModel: VideoViewModel by viewModels()
        viewModel.playWhenReady = player!!.playWhenReady
        viewModel.playbackPosition = player!!.currentPosition
        viewModel.currentWindow = player!!.currentWindowIndex
        player!!.release()
        player = null
    }
}