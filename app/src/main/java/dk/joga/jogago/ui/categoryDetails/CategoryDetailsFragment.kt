package dk.joga.jogago.ui.categoryDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentCategoryBinding

class CategoryDetailsFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val args: CategoryDetailsFragmentArgs by navArgs()
    private val adapter = CategoryDetailsAdapter()
    private var like = false
    private lateinit var viewModelFactory: CategoryDetailsViewModelFactory
    private lateinit var viewModel: CategoryDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        viewModelFactory = CategoryDetailsViewModelFactory(args.categoryId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CategoryDetailsViewModel::class.java)

        val recyclerView = binding.categoryClassesRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.categoryClassesWrapper.getData().observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success) {
                val firstClass = resource.data!!.first()
                val otherClasses = resource.data - firstClass
                binding.categoryName.text = args.categoryName
                Glide.with(this)
                        .load(firstClass.thumbnailUrl)
                        .fallback(R.drawable.placeholder_image)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.categoryClassThumbnail)
                @Suppress("SENSELESS_COMPARISON")
                if (firstClass.userLike.classId != null) {
                    like = true
                    binding.categoryFirstClassLike.isSelected = like
                } else {
                    like = false
                    binding.categoryFirstClassLike.isSelected = like
                }
                binding.categoryFirstClassLike.setOnClickListener {
                    AppContainer.repository.toggleClassLike(firstClass.id)
                    if (like){
                        like = false
                        binding.categoryFirstClassLike.isSelected = like
                    } else {
                        like = true
                        binding.categoryFirstClassLike.isSelected = like
                    }
                }
                binding.firstClassName.text = firstClass.title
                binding.firstClassInstructorName.text = firstClass.instructor.name
                binding.firstClassDuration.text = getString(R.string.min, firstClass.duration)
                binding.categoryFirstClassDescription.text = firstClass.description
                binding.firstClassThumbnail.setOnClickListener {
                    val action = CategoryDetailsFragmentDirections.actionCategoryFragmentToClassDetailsFragment(firstClass.id)
                    findNavController().navigate(action)
                }
                adapter.setData(otherClasses)
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}