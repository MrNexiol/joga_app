package com.prograils.joga.ui.trainerDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.prograils.joga.JoGaApplication
import com.prograils.joga.databinding.FragmentTrainerDetailBinding

class TrainerDetailFragment : Fragment() {
    private var _binding: FragmentTrainerDetailBinding? = null
    private val binding get() = _binding!!
    private val args: TrainerDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainerDetailBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        val viewModel: TrainerDetailViewModel by viewModels { TrainerDetailViewModelFactory(appContainer.repository, args.trainerId) }
        val recyclerView = binding.instructorClassesRecyclerView
        val adapter = TrainerDetailAdapter(listOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.instructor.observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                Glide.with(this).load(it.avatar_url).into(binding.instructorAvatar)
                binding.instructorNameTextView.text = it.name
            }
        })
        viewModel.trainerClasses.observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                if (it.isEmpty()){
                    binding.noClassesTextView.visibility = View.VISIBLE
                } else {
                    adapter.setData(it)
                }
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}