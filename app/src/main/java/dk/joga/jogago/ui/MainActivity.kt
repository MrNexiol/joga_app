package dk.joga.jogago.ui

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import dk.joga.jogago.R
import dk.joga.jogago.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.loginErrorFragment, R.id.popupFragment, R.id.logoutFragment -> binding.mainBottomNav.visibility = View.GONE
                R.id.classDetailsFragment -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        binding.mainBottomNav.visibility = View.GONE
                    }
                }
                else -> binding.mainBottomNav.visibility = View.VISIBLE
            }
        }

        binding.mainBottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    navController.navigate(R.id.action_global_homeFragment)
                    true
                }
                R.id.navigation_classes -> {
                    navController.navigate(R.id.action_global_classesFragment)
                    true
                }
                else -> false
            }
        }
    }
}