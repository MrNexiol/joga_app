package com.prograils.joga.ui.popup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.prograils.joga.databinding.FragmentPopupBinding

class PopupFragment : Fragment() {

    private var _binding: FragmentPopupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.letsGoButton.setOnClickListener {
            val action = PopupFragmentDirections.actionPopupFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
}