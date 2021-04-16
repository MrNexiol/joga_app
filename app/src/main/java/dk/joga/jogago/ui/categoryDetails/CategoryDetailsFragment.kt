package dk.joga.jogago.ui.categoryDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentCategoryBinding
import dk.joga.jogago.ui.MainActivity

class CategoryDetailsFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val args: CategoryDetailsFragmentArgs by navArgs()
    private val adapter = CategoryDetailsAdapter()
    private var itemsCount = 0
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
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val tmp = layoutManager.findLastVisibleItemPosition()
                if (layoutManager.findLastVisibleItemPosition() >= itemsCount - 1) {
                    Log.d("Siemanko", "Testowańsko $tmp")
                }
            }
        })

        viewModel.categoryClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
                (requireActivity() as MainActivity).changeScreenTitle(args.categoryName)
                adapter.setData(resource.data!!)
                itemsCount = resource.data.count()
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