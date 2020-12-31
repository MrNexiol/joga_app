package com.prograils.joga.ui.liked

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.prograils.joga.JoGaApplication
import com.prograils.joga.databinding.FragmentLikedBinding

class LikedFragment : Fragment() {
    private var _binding: FragmentLikedBinding? = null
    private val binding get() = _binding!!
    private val args: LikedFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        val viewModel: LikedViewModel by viewModels { LikedViewModelFactory(appContainer.repository, args.userToken) }

        val recyclerView = binding.likedClassesRecyclerView
        val adapter = LikedAdapter(listOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.likedClasses.observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                adapter.setData(it)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}