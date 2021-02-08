package com.prograils.joga.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.prograils.joga.JoGaApplication
import com.prograils.joga.R
import com.prograils.joga.api.Status
import com.prograils.joga.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val token = sharedPrefs?.getString(getString(R.string.saved_token_key), null)
        if (token != null){
            navigateToPopup()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appContainer = (activity?.application as JoGaApplication).appContainer
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val imm: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            appContainer.repository.login(username, password).observe(viewLifecycleOwner, { resource ->
                resource.data?.let {
                    with(sharedPrefs?.edit()){
                        this?.putString(getString(R.string.saved_token_key), it.token)
                        this?.putString(getString(R.string.saved_user_id), it.userId)
                        this?.putString(getString(R.string.saved_username), username)
                        this?.commit()
                    }
                    navigateToPopup()
                }
                if (resource.status == Status.Fail){
                    val action = LoginFragmentDirections.actionLoginFragmentToLoginErrorFragment()
                    findNavController().navigate(action)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToPopup() {
        val action = LoginFragmentDirections.actionLoginFragmentToPopupFragment()
        findNavController().navigate(action)
    }
}