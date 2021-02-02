package com.prograils.joga.ui.journeys

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.prograils.joga.GridSpacingItemDecoration
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.databinding.FragmentJourneysBinding

class JourneysFragment : Fragment() {
    private var _binding: FragmentJourneysBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJourneysBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer

        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)

        val journeysViewModel: JourneysViewModel by viewModels { JourneysViewModelFactory(appContainer.repository, token!!) }

        val recyclerView = binding.journeysRecyclerView
        val adapter = JourneysAdapter(listOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(resources.getDimension(R.dimen.journey_list_decoration).toInt()))

        journeysViewModel.journeysWrapper.getData().observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                adapter.setData(it)
            }
        })

        binding.journeysBottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    findNavController().navigate(R.id.action_global_homeFragment)
                    true
                }
                R.id.navigation_classes -> {
                    findNavController().navigate(R.id.action_global_classesFragment)
                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}