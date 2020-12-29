package com.prograils.joga.ui.journeys

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prograils.joga.GridSpacingItemDecoration
import com.prograils.joga.JoGaApplication
import com.prograils.joga.databinding.FragmentJourneysBinding

class JourneysFragment : Fragment() {
    private var _binding: FragmentJourneysBinding? = null
    private val binding get() = _binding!!
    private lateinit var journeysViewModel: JourneysViewModel
    private lateinit var journeysRecyclerView: RecyclerView
    private lateinit var journeysAdapter: JourneysAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJourneysBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        journeysViewModel = JourneysViewModel(appContainer.repository)

        journeysRecyclerView = binding.journeysRecyclerView
        journeysAdapter = JourneysAdapter(listOf())
        journeysRecyclerView.adapter = journeysAdapter
        journeysRecyclerView.layoutManager = GridLayoutManager(context, 2)
        journeysRecyclerView.addItemDecoration(GridSpacingItemDecoration(40))

        journeysViewModel.journeys.observe(viewLifecycleOwner, {
            journeysAdapter.setData(it.journeys)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}