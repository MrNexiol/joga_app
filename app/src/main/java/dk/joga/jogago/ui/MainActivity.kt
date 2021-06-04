package dk.joga.jogago.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.cast.framework.CastButtonFactory
import dk.joga.jogago.AppContainer
import dk.joga.jogago.R
import dk.joga.jogago.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var likeId = ""
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CastButtonFactory.setUpMediaRouteButton(this, binding.castButton)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    changeScreenTitle(getString(R.string.train_now))
                    headerVisible(true)
                    bottomNavVisible(true)
                    likeIconVisible(false)
                }
                R.id.classesFragment -> {
                    changeScreenTitle(getString(R.string.classes))
                    likeIconVisible(false)
                }
                R.id.categoryFragment -> {
                    likeIconVisible(false)
                }
                R.id.likedFragment -> {
                    changeScreenTitle(getString(R.string.liked_by_you))
                    likeIconVisible(false)
                }
                R.id.journeyDetailsFragment -> {
                    likeIconVisible(false)
                }
                R.id.journeysFragment -> {
                    changeScreenTitle(getString(R.string.journeys))
                }
                R.id.trainerDetailFragment -> {
                    likeIconVisible(false)
                }
                R.id.loginFragment, R.id.loginErrorFragment, R.id.popupFragment, R.id.logoutFragment, R.id.subscriptionErrorFragment -> {
                    bottomNavVisible(false)
                    headerVisible(false)
                }
                R.id.classDetailsFragment -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        headerVisible(false)
                        bottomNavVisible(false)
                        hideSystemUi()
                        binding.navHostFragment.setPadding(0,0,0,0)
                    } else {
                        headerVisible(true)
                        bottomNavVisible(true)
                        likeIconVisible(true)
                        binding.navHostFragment.setPadding(resources.getDimensionPixelSize(R.dimen.default_app_padding),0,resources.getDimensionPixelSize(R.dimen.default_app_padding),0)
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

        binding.likeIcon.setOnClickListener {
            AppContainer.repository.toggleClassLike(likeId)
            binding.likeIcon.isSelected = !binding.likeIcon.isSelected
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination!!.id == R.id.classDetailsFragment && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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

    private fun likeIconVisible(visible: Boolean) {
        val modifier = if (visible) View.VISIBLE else View.GONE
        binding.likeIcon.visibility = modifier
    }

    private fun headerVisible(visible: Boolean) {
        val modifier = if (visible) View.VISIBLE else View.GONE
        binding.screenTitle.visibility = modifier
        binding.castButton.visibility = modifier
        binding.topDivider.visibility = modifier
    }

    private fun bottomNavVisible(visible: Boolean) {
        val modifier = if (visible) View.VISIBLE else View.GONE
        binding.mainBottomNav.visibility = modifier
    }

    fun changeScreenTitle(title: String) {
        binding.screenTitle.text = title
    }

    fun setClassId(id: String) {
        this.likeId = id
    }

    fun setLikeIcon(liked: Boolean) {
        binding.likeIcon.isSelected = liked
    }

    fun subscriptionError() {
        navController.navigate(R.id.action_global_subscriptionErrorFragment)
    }

    fun preventScreenLocking() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun allowScreenLocking() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun logout() {
        val sharedPrefs = getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE)
        with(sharedPrefs?.edit()){
            this?.remove(getString(R.string.saved_token_key))
            this?.remove(getString(R.string.saved_refresh_token_key))
            this?.remove(getString(R.string.saved_user_id))
            this?.remove(getString(R.string.saved_username))
            this?.remove(getString(R.string.saved_password))
            this?.apply()
        }
        Toast.makeText(this,getString(R.string.connection_error),Toast.LENGTH_SHORT).show()
        navController.navigate(R.id.action_global_loginFragment)
    }
}