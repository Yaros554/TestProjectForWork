package com.skyyaros.android.testprojectforwork.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skyyaros.android.testprojectforwork.App
import com.skyyaros.android.testprojectforwork.R
import com.skyyaros.android.testprojectforwork.databinding.ActivityMainBinding
import com.skyyaros.android.testprojectforwork.entity.UserInfo
import kotlinx.coroutines.flow.StateFlow

class MainActivity : AppCompatActivity(), ActivityCallbacks {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels {
        object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(App.component.getDatabaseRepository()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    navController.popBackStack(R.id.homeFragment, inclusive = false)
                }

                R.id.catalog -> {
                    navController.popBackStack(R.id.catalogFragment, inclusive = false)
                }

                R.id.bag -> {
                    navController.popBackStack(R.id.bagFragment, inclusive = false)
                }

                R.id.action -> {
                    navController.popBackStack(R.id.actionFragment, inclusive = false)
                }

                else -> {
                    navController.popBackStack(R.id.profileFragment, inclusive = false)
                }
            }
        }
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment, R.id.catalogFragment, R.id.bagFragment, R.id.actionFragment, R.id.profileFragment,
            R.id.registerFragment
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun showDownBar() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    override fun hideDownBar() {
        binding.bottomNav.visibility = View.GONE
    }

    override fun editUpBar(label: String, isRoot: Boolean) {
        binding.toolbar.title = ""
        binding.toolbarTitle.text = label
        if (!isRoot)
            binding.toolbar.setNavigationIcon(R.drawable.icon_back)
    }

    override fun getUsers(): StateFlow<List<UserInfo>> {
        return viewModel.usersFlow
    }
}