package dk.joga.jogago.ui.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dk.joga.jogago.GridSpacingItemDecoration
import dk.joga.jogago.R
import dk.joga.jogago.databinding.FragmentClassesBinding

class ClassesFragment : Fragment() {
    private var _binding: FragmentClassesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ClassesViewModel by viewModels()
    private val adapter = ClassesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassesBinding.inflate(inflater, container, false)
        val recyclerView = binding.classesRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(resources.getDimension(R.dimen.journey_list_decoration).toInt()))

        viewModel.categoriesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                adapter.setData(it)
            }
        })

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