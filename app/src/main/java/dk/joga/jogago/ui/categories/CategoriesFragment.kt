package dk.joga.jogago.ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dk.joga.jogago.AppContainer
import dk.joga.jogago.GridSpacingItemDecoration
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentCategoriesBinding
import dk.joga.jogago.ui.MainActivity

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoriesViewModel by viewModels()
    private val adapter = CategoriesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val recyclerView = binding.classesRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(resources.getDimension(R.dimen.journey_list_decoration).toInt()))

        viewModel.categoriesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.Success -> adapter.setData(resource.data!!)
                Status.Empty -> adapter.setData(listOf())
                Status.SubscriptionEnded -> (activity as MainActivity).subscriptionError()
                else -> Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show()
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

    override fun onResume() {
        super.onResume()
        AppContainer.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "categories")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ClassesFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}