package dk.joga.jogago.ui.subscription_error

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentSubscriptionErrorBinding

class SubscriptionErrorFragment : Fragment() {

    private var _binding: FragmentSubscriptionErrorBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSubscriptionErrorBinding.inflate(layoutInflater)
        sharedPrefs = requireActivity().getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.refreshButton.setOnClickListener {
            val username = sharedPrefs.getString(getString(R.string.saved_username), null)
            val password = sharedPrefs.getString(getString(R.string.saved_password), null)
            if (username != null && password != null) {
                AppContainer.repository.login(username, password).observe(viewLifecycleOwner, { resource ->
                    when (resource.status) {
                        Status.Success -> {
                            with(sharedPrefs.edit()) {
                                this?.remove(getString(R.string.saved_token_key))
                                this?.putString(getString(R.string.saved_token_key), resource.data!!.token)
                                this?.commit()
                            }
                            findNavController().navigate(R.id.action_global_homeFragment)
                        }
                        Status.SubscriptionEnded -> Toast.makeText(context, R.string.no_subscription_active, Toast.LENGTH_LONG).show()
                        else -> Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(context, R.string.sub_login_error, Toast.LENGTH_LONG).show()
            }
        }
        binding.noSubscriptionLogoutButton.setOnClickListener {
            AppContainer.repository.logout().observe(viewLifecycleOwner, {
                with(sharedPrefs.edit()){
                    this?.remove(getString(R.string.saved_token_key))
                    this?.remove(getString(R.string.saved_user_id))
                    this?.remove(getString(R.string.saved_username))
                    this?.apply()
                }
                findNavController().navigate(R.id.action_global_loginFragment)
            })
        }
    }
}