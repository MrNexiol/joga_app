package dk.joga.jogago.ui.classes

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dk.joga.jogago.GridSpacingItemDecoration
import dk.joga.jogago.JoGaApplication
import dk.joga.jogago.R
import dk.joga.jogago.databinding.FragmentClassesBinding

class ClassesFragment : Fragment() {
    private var _binding: FragmentClassesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ClassesViewModel
    private lateinit var viewModelFactory: ClassesViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassesBinding.inflate(inflater, container, false)

        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)

        val appContainer = (activity?.application as JoGaApplication).appContainer
        viewModelFactory = ClassesViewModelFactory(appContainer.repository, token!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClassesViewModel::class.java)

        val recyclerView = binding.classesRecyclerView
        val adapter = ClassesAdapter(listOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(resources.getDimension(R.dimen.journey_list_decoration).toInt()))

        viewModel.categoriesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                adapter.setData(it)
            }
        })

        binding.classesBottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    findNavController().navigate(R.id.action_global_homeFragment)
                    true
                }
                R.id.navigation_classes -> {
                    binding.classesRecyclerView.smoothScrollToPosition(0)
                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.classesRefreshLayout.setOnRefreshListener {
            viewModel.refreshClasses()
            binding.classesRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}