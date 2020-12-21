package com.prograils.joga.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.prograils.joga.JoGaApplication
import com.prograils.joga.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appContainer = (activity?.application as JoGaApplication).appContainer
        homeViewModel = HomeViewModel(appContainer.repository)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel.instructors.observe(viewLifecycleOwner, {
            binding.tester.text = it.toString()
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}