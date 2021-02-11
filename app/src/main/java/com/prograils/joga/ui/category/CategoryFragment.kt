package com.prograils.joga.ui.category

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.api.Status
import com.prograils.joga.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val args: CategoryFragmentArgs by navArgs()
    private lateinit var viewModelFactory: CategoryViewModelFactory
    private lateinit var viewModel: CategoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)
        viewModelFactory = CategoryViewModelFactory(appContainer.repository, token!!, args.categoryId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CategoryViewModel::class.java)

        viewModel.categoryClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
                val firstClass = resource.data!!.first()
                binding.categoryName.text = args.categoryName
                Glide.with(this)
                    .load(firstClass.thumbnailUrl)
                    .fallback(R.drawable.placeholder_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.categoryClassThumbnail)
                binding.firstClassName.text = firstClass.title
                binding.firstClassInstructorName.text = firstClass.instructor.name
                binding.firstClassDuration.text = getString(R.string.min, firstClass.duration)
                binding.categoryFirstClassDescription.text = firstClass.description
                binding.firstClassThumbnail.setOnClickListener {
                    val action = CategoryFragmentDirections.actionCategoryFragmentToClassDetailsFragment(firstClass.id)
                    findNavController().navigate(action)
                }
            }
        })

        return binding.root
    }
}