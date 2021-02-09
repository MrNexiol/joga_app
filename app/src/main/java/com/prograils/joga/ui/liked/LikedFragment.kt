package com.prograils.joga.ui.liked

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.api.Status
import com.prograils.joga.databinding.FragmentLikedBinding

class LikedFragment : Fragment() {
    private var _binding: FragmentLikedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)
        val viewModel: LikedViewModel by viewModels { LikedViewModelFactory(appContainer.repository, token!!) }

        val recyclerView = binding.likedClassesRecyclerView
        val adapter = LikedAdapter(listOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.likedClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success){
                adapter.setData(resource.data!!)
            } else {
                binding.noLikedClasses.visibility = View.VISIBLE
            }
        })

        binding.likedBottomNavigation.setOnNavigationItemSelectedListener {
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