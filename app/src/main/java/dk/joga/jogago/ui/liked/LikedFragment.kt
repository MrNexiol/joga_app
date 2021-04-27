package dk.joga.jogago.ui.liked

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentLikedBinding

class LikedFragment : Fragment() {
    private var _binding: FragmentLikedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LikedViewModel by viewModels()
    private val adapter = LikedAdapter()
    private var itemsCount = 0
    private var isMore = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedBinding.inflate(inflater, container, false)

        viewModel.likedClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success){
                if (viewModel.isLoading) {
                    adapter.addData(resource.data!!)
                    itemsCount += resource.data.count()
                    viewModel.isLoading = false
                } else {
                    if (!viewModel.wasRendered) {
                        adapter.setData(resource.data!!)
                        itemsCount = resource.data.count()
                        viewModel.wasRendered = true
                    }
                }
                if (itemsCount == resource.totalCount) {
                    isMore = false
                }
            } else {
                adapter.setData(listOf())
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
                if (layoutManager.findLastVisibleItemPosition() >= itemsCount - 1 && isMore) {
                    viewModel.changePageNumber()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}