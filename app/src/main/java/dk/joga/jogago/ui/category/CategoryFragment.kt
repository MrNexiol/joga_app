package dk.joga.jogago.ui.category

import android.content.Context
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
import dk.joga.jogago.JoGaApplication
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentCategoryBinding

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

        val recyclerView = binding.categoryClassesRecyclerView
        val adapter = CategoryAdapter(listOf(), appContainer.repository, token)
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
                binding.firstClassName.text = firstClass.title
                binding.firstClassInstructorName.text = firstClass.instructor.name
                binding.firstClassDuration.text = getString(R.string.min, firstClass.duration)
                binding.categoryFirstClassDescription.text = firstClass.description
                binding.firstClassThumbnail.setOnClickListener {
                    val action = CategoryFragmentDirections.actionCategoryFragmentToClassDetailsFragment(firstClass.id)
                    findNavController().navigate(action)
                }
                adapter.setData(otherClasses)
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.categoryBottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home -> {
                    findNavController().navigate(R.id.action_global_homeFragment)
                    true
                }
                R.id.navigation_classes -> {
                    findNavController().navigateUp()
                    true
                }
                else -> false
            }
        }
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