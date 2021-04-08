package dk.joga.jogago.ui.categoryDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentCategoryBinding
import dk.joga.jogago.ui.MainActivity

class CategoryDetailsFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val args: CategoryDetailsFragmentArgs by navArgs()
    private val adapter = CategoryDetailsAdapter()
    private lateinit var viewModelFactory: CategoryDetailsViewModelFactory
    private lateinit var viewModel: CategoryDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        viewModelFactory = CategoryDetailsViewModelFactory(args.categoryId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CategoryDetailsViewModel::class.java)

        val recyclerView = binding.categoryClassesRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.categoryClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
                (requireActivity() as MainActivity).changeScreenTitle(args.categoryName)
                adapter.setData(resource.data!!)
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}