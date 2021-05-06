package dk.joga.jogago.ui.categoryDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dk.joga.jogago.AppContainer
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
                if (layoutManager.findLastVisibleItemPosition() >= itemsCount - 1) {
                    viewModel.changePageNumber()
                }
            }
        })

        viewModel.categoryClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
                (requireActivity() as MainActivity).changeScreenTitle(args.categoryName)
                if (viewModel.isLoading) {
                    adapter.addData(resource.data!!)
                    itemsCount += resource.data.count()
                    viewModel.isLoading = false
                } else {
                    adapter.setData(resource.data!!)
                    itemsCount = resource.data.count()
                }
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnRefreshListener {
            viewModel.resetData()
            binding.root.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        AppContainer.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "category_details")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "CategoryDetailsFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}