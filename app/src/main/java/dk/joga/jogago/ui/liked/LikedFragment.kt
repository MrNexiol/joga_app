package dk.joga.jogago.ui.liked

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dk.joga.jogago.AppContainer
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentLikedBinding

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
        viewModelFactory = LikedViewModelFactory(AppContainer.repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LikedViewModel::class.java)

        val recyclerView = binding.likedClassesRecyclerView
        val adapter = LikedAdapter(listOf(), AppContainer.repository)
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