package dk.joga.jogago.ui.logout

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentLogoutBinding
import dk.joga.jogago.BuildConfig

class LogoutFragment : Fragment() {
    private var _binding: FragmentLogoutBinding? = null
    private val binding get() = _binding!!
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        sharedPrefs = requireActivity().getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.usernameTextView.text = sharedPrefs?.getString(getString(R.string.saved_username), "")
        binding.currentAppVersionTextView.text = BuildConfig.VERSION_NAME
        binding.logMeOutButton.setOnClickListener {
            AppContainer.repository.logout().observe(viewLifecycleOwner, { resource ->
                if (resource.status == Status.Success){
                    with(sharedPrefs?.edit()){
                        this?.remove(getString(R.string.saved_token_key))
                        this?.remove(getString(R.string.saved_user_id))
                        this?.remove(getString(R.string.saved_username))
                        this?.apply()
                    }
                    findNavController().navigate(R.id.action_global_loginFragment)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        AppContainer.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "logout")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "LogoutFragment")
        }
    }
}