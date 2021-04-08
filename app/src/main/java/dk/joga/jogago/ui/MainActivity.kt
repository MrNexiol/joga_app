package dk.joga.jogago.ui

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dk.joga.jogago.R
import dk.joga.jogago.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.loginErrorFragment, R.id.popupFragment, R.id.logoutFragment -> binding.mainBottomNav.visibility = View.GONE
                R.id.classDetailsFragment -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        binding.mainBottomNav.visibility = View.GONE
                        hideSystemUi()
                    }
                }
                else -> binding.mainBottomNav.visibility = View.VISIBLE
            }
        }

        binding.mainBottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    if (navController.currentDestination!!.id != R.id.homeFragment) {
                        navController.navigate(R.id.action_global_homeFragment)
                    }
                    true
                }
                R.id.navigation_classes -> {
                    if (navController.currentDestination!!.id != R.id.classesFragment) {
                        navController.navigate(R.id.action_global_classesFragment)
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (navController.currentDestination!!.id == R.id.classDetailsFragment) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun hideSystemUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }
}