package com.prograils.joga.ui.popup

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.api.Status
import com.prograils.joga.databinding.FragmentPopupBinding
import java.util.*

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
        val wasSeen = sharedPrefs?.getString(getString(R.string.was_seen) + getString(R.string.saved_user_id), null)
        if (wasSeen != null){
            navigateToHome()
        }
        val appContainer = (activity?.application as JoGaApplication).appContainer
        val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)
        val viewModel: PopupViewModel by viewModels { PopupViewModelFactory(appContainer.repository, token!!) }
        viewModel.welcomePopup.observe(viewLifecycleOwner, { resource ->
            if (resource.status == Status.Success){
                if (Locale.getDefault().language == "da"){
                    binding.popupWelcomeTextView.text = resource.data!!.titleDa
                    binding.popupMessageTextView.text = resource.data.textDa
                } else {
                    binding.popupWelcomeTextView.text = resource.data!!.titleEn
                    binding.popupMessageTextView.text = resource.data.textEn
                }
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.letsGoButton.setOnClickListener {
            with(sharedPrefs?.edit()){
                this?.putString(getString(R.string.was_seen) + getString(R.string.saved_user_id), "seen")
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