package dk.joga.jogago.ui.journeys

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dk.joga.jogago.AppContainer
import dk.joga.jogago.GridSpacingItemDecoration
import dk.joga.jogago.R
import dk.joga.jogago.databinding.FragmentJourneysBinding

class JourneysFragment : Fragment() {
    private var _binding: FragmentJourneysBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JourneysViewModel by viewModels()
    private val adapter = JourneysAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJourneysBinding.inflate(inflater, container, false)
        val recyclerView = binding.journeysRecyclerView
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

    override fun onResume() {
        super.onResume()
        AppContainer.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "journeys")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "JourneysFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}