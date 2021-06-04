package dk.joga.jogago.ui.liked

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentLikedBinding
import dk.joga.jogago.ui.MainActivity

class LikedFragment : Fragment() {
    private var _binding: FragmentLikedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LikedViewModel by viewModels()
    private val adapter = LikedAdapter()
    private var itemsCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedBinding.inflate(inflater, container, false)

        viewModel.likedClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.Success -> {
                    if (itemsCount + resource.data!!.size == resource.totalCount) {
                        viewModel.isMore = false
                    }

                    if (viewModel.isLoading) {
                        adapter.addData(resource.data, viewModel.isMore)
                        itemsCount += resource.data.size
                        viewModel.isLoading = false
                    } else {
                        adapter.setData(resource.data, viewModel.isMore)
                        itemsCount = resource.data.size
                    }
                }
                Status.Empty -> adapter.setData(listOf(), false)
                Status.Unauthorized -> (activity as MainActivity).logout()
                Status.SubscriptionEnded -> (activity as MainActivity).subscriptionError()
                else -> Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.likedRefreshLayout.setOnRefreshListener {
            viewModel.resetData()
            binding.likedRefreshLayout.isRefreshing = false
        }

        val recyclerView = binding.likedClassesRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastVisibleItemPosition() >= itemsCount - 1 && viewModel.isMore) {
                    viewModel.changePageNumber()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        AppContainer.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "liked_classes")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "LikedFragment")
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.resetData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}