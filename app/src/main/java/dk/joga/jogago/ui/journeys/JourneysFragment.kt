package dk.joga.jogago.ui.journeys

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import dk.joga.jogago.GridSpacingItemDecoration
import dk.joga.jogago.JoGaApplication
import dk.joga.jogago.R
import dk.joga.jogago.databinding.FragmentJourneysBinding

class JourneysFragment : Fragment() {
    private var _binding: FragmentJourneysBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: JourneysViewModel
    private lateinit var viewModelFactory: JourneysViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJourneysBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        viewModelFactory = JourneysViewModelFactory(appContainer.repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(JourneysViewModel::class.java)

        val recyclerView = binding.journeysRecyclerView
        val adapter = JourneysAdapter(listOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(resources.getDimension(R.dimen.journey_list_decoration).toInt()))

        viewModel.journeysWrapper.getData().observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                adapter.setData(it)
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.journeysSwipeRefresh.setOnRefreshListener {
            viewModel.refreshJourneys()
            binding.journeysSwipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}