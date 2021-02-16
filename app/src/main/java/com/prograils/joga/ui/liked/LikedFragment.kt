package com.prograils.joga.ui.liked

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.api.Status
import com.prograils.joga.databinding.FragmentLikedBinding

class LikedFragment : Fragment() {
    private var _binding: FragmentLikedBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelFactory: LikedViewModelFactory
    private lateinit var viewModel: LikedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)
        viewModelFactory = LikedViewModelFactory(appContainer.repository, token!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LikedViewModel::class.java)

        val recyclerView = binding.likedClassesRecyclerView
        val adapter = LikedAdapter(listOf(), appContainer.repository, token)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.likedClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success){
                adapter.setData(resource.data!!)
                binding.noLikedClasses.visibility = View.INVISIBLE
            } else {
                adapter.setData(listOf())
                binding.noLikedClasses.visibility = View.VISIBLE
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.likedRefreshLayout.setOnRefreshListener {
            viewModel.refreshLikedClasses()
            binding.likedRefreshLayout.isRefreshing = false
        }

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
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshLikedClasses()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}