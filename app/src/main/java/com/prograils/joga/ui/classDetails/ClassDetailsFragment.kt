package com.prograils.joga.ui.classDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.prograils.joga.databinding.FragmentClassDetailsBinding

class ClassDetailsFragment : Fragment() {
    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ClassDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tester.text = args.classId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}