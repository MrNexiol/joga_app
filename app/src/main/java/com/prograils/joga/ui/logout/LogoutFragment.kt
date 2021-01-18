package com.prograils.joga.ui.logout

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.api.Status
import com.prograils.joga.databinding.FragmentLogoutBinding
import com.prograils.joga.BuildConfig

class LogoutFragment : Fragment() {
    private var _binding: FragmentLogoutBinding? = null
    private val binding get() = _binding!!
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        binding.usernameTextView.text = sharedPrefs?.getString(getString(R.string.saved_username), "")
        binding.currentAppVersionTextView.text = BuildConfig.VERSION_NAME
        binding.logMeOutButton.setOnClickListener {
            val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)
            token?.let {
                appContainer.repository.logout(it).observe(viewLifecycleOwner, { resource ->
                    if (resource.status == Status.Success){
                        with(sharedPrefs?.edit()){
                            this?.remove(getString(R.string.saved_token_key))
                            this?.remove(getString(R.string.saved_user_id))
                            this?.remove(getString(R.string.saved_username))
                            this?.apply()
                        }
                        val action = LogoutFragmentDirections.actionLogoutFragmentToLoginFragment()
                        findNavController().navigate(action)
                    }
                })
            }
        }
    }
}