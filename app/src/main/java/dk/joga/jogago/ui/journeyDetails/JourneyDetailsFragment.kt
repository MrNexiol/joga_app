package dk.joga.jogago.ui.journeyDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentJourneyDetailsBinding
import dk.joga.jogago.ui.MainActivity

class JourneyDetailsFragment : Fragment() {
    private var _binding: FragmentJourneyDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: JourneyDetailsFragmentArgs by navArgs()
    private lateinit var viewModelFactory: JourneyViewModelFactory
    private lateinit var viewModel: JourneyViewModel
    private val adapter = JourneyDetailsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJourneyDetailsBinding.inflate(inflater, container, false)
        viewModelFactory = JourneyViewModelFactory(args.journeyId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(JourneyViewModel::class.java)

        val recyclerView = binding.journeysRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.journeyWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
                (requireActivity() as MainActivity).changeScreenTitle(resource.data!!.name)
                adapter.setData(resource.data.classes)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}