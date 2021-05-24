package dk.joga.jogago.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        sharedPrefs = activity?.getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            val imm: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            AppContainer.repository.login(username, password).observe(viewLifecycleOwner, { resource ->
                when (resource.status) {
                    Status.Success -> {
                        with(sharedPrefs?.edit()){
                            this?.putString(getString(R.string.saved_token_key), resource.data!!.token)
                            this?.putString(getString(R.string.saved_user_id), resource.data!!.userId)
                            this?.putString(getString(R.string.saved_username), username)
                            this?.commit()
                        }
                        navigateToHome()
                    }
                    else -> {
                        val action = LoginFragmentDirections.actionLoginFragmentToLoginErrorFragment()
                        findNavController().navigate(action)
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        AppContainer.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "login")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "LoginFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToHome() {
        val action = LoginFragmentDirections.actionGlobalHomeFragment()
        findNavController().navigate(action)
    }
}