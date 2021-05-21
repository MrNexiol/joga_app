package dk.joga.jogago.ui.popup

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dk.joga.jogago.AppContainer
import dk.joga.jogago.JoGaApplication
import dk.joga.jogago.R
import dk.joga.jogago.api.Status
import dk.joga.jogago.databinding.FragmentPopupBinding
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
//        val wasSeen = sharedPrefs?.getString(getString(R.string.was_seen), null)
//        if (wasSeen != null){
//            navigateToHome()
//        }
        val viewModel: PopupViewModel by viewModels { PopupViewModelFactory(AppContainer.repository) }
//        viewModel.welcomePopup.observe(viewLifecycleOwner, { resource ->
//            if (resource.status == Status.Success){
//                if (Locale.getDefault().language == "da"){
//                    binding.popupWelcomeTextView.text = resource.data!!.titleDa
//                    binding.popupMessageTextView.text = resource.data.textDa
//                } else {
//                    binding.popupWelcomeTextView.text = resource.data!!.titleEn
//                    binding.popupMessageTextView.text = resource.data.textEn
//                }
//            }
//        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(sharedPrefs?.edit()){
            this?.putString(getString(R.string.was_seen), "seen")
            this?.commit()
        }
        binding.letsGoButton.setOnClickListener {
//            navigateToHome()
        }
    }

    override fun onResume() {
        super.onResume()
        AppContainer.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "popup")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "PopupFragment")
        }
    }
//
//    private fun navigateToHome(){
//        val action = PopupFragmentDirections.actionPopupFragmentToHomeFragment()
//        findNavController().navigate(action)
//    }
}