package com.prograils.joga.ui.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.navigation.fragment.navArgs
import com.prograils.joga.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private val args: VideoFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.videoView.setVideoPath(args.videoUrl)
        binding.videoView.setMediaController(MediaController(context))
        binding.videoView.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}