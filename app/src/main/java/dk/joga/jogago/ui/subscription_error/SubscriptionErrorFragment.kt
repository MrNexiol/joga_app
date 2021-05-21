package dk.joga.jogago.ui.subscription_error

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.joga.jogago.databinding.FragmentSubscriptionErrorBinding

class SubscriptionErrorFragment : Fragment() {

    private var _binding: FragmentSubscriptionErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionErrorBinding.inflate(layoutInflater)
        return binding.root
    }
}