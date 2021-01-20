package com.prograils.joga.ui.popup

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.prograils.joga.R
import com.prograils.joga.databinding.FragmentPopupBinding

class PopupFragment : Fragment() {

    private var _binding: FragmentPopupBinding? = null
    private val binding get() = _binding!!
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopupBinding.inflate(inflater, container, false)
        sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val wasSeen = sharedPrefs?.getString(getString(R.string.was_seen), null)
        if (wasSeen != null){
            navigateToHome()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.letsGoButton.setOnClickListener {
            with(sharedPrefs?.edit()){
                this?.putString(getString(R.string.was_seen), "seen")
                this?.commit()
            }
            navigateToHome()
        }
    }

    private fun navigateToHome(){
        val action = PopupFragmentDirections.actionPopupFragmentToHomeFragment()
        findNavController().navigate(action)
    }
}