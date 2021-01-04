package com.prograils.joga.ui.classDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.databinding.FragmentClassDetailsBinding

class ClassDetailsFragment : Fragment() {
    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ClassDetailsFragmentArgs by navArgs()
    private var tmp = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater, container, false)

        val appContainer = (activity?.application as JoGaApplication).appContainer
        val viewModel: ClassDetailsViewModel by viewModels { ClassDetailsViewModelFactory(appContainer.repository, args.classId) }

        viewModel.singleClass.observe(viewLifecycleOwner, { resource ->
            resource.data?.let {
                binding.className.text = it.title
                Glide.with(this).load(it.thumbnailUrl).into(binding.classThumbnail)
                binding.classTitleDuration.text = getString(R.string.title_duration, it.title, it.duration)
                binding.classDescription.text = it.description
                Glide.with(this).load(it.instructor.avatar_url).into(binding.classInstructorAvatar)
                binding.classInstructorName.text = it.instructor.name
                binding.classInstructorRoot.setOnClickListener { _ ->
                    val action = ClassDetailsFragmentDirections.actionClassDetailsFragmentToTrainerDetailFragment(it.instructor.id)
                    findNavController().navigate(action)
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.likeButton.setOnClickListener {
            tmp = if (tmp){
                binding.likeButton.setImageResource(R.drawable.heart_not_liked)
                false
            } else {
                binding.likeButton.setImageResource(R.drawable.heart_liked)
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}