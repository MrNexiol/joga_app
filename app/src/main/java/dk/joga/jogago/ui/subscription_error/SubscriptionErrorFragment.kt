package dk.joga.jogago.ui.subscription_error

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.databinding.FragmentSubscriptionErrorBinding

class SubscriptionErrorFragment : Fragment() {

    private var _binding: FragmentSubscriptionErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSubscriptionErrorBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPrefs = requireActivity().getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE)
        binding.noSubscriptionLogoutButton.setOnClickListener {
            AppContainer.repository.logout().observe(viewLifecycleOwner, {
                with(sharedPrefs?.edit()){
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