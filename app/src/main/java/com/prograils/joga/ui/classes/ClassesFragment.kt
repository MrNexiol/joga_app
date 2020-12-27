package com.prograils.joga.ui.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.databinding.FragmentClassesBinding

class ClassesFragment : Fragment() {
    private var _binding: FragmentClassesBinding? = null
    private val binding get() = _binding!!
    private lateinit var classesViewModel: ClassesViewModel
    private lateinit var classesRecyclerView: RecyclerView
    private lateinit var classesAdapter: ClassesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassesBinding.inflate(inflater, container, false)

        val appContainer = (activity?.application as JoGaApplication).appContainer
        classesViewModel = ClassesViewModel(appContainer.repository)

        classesRecyclerView = binding.classesRecyclerView
        classesAdapter = ClassesAdapter(listOf(), this)
        classesRecyclerView.adapter = classesAdapter
        classesRecyclerView.layoutManager = GridLayoutManager(context, 2)
        classesRecyclerView.addItemDecoration(GridSpacingItemDecoration(40))

        classesViewModel.classes.observe(viewLifecycleOwner, {
            classesAdapter.setData(it.classes)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.classesBottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    val action = ClassesFragmentDirections.actionClassesFragmentToHomeFragment()
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}