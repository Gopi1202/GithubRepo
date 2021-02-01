package com.gopi.githubrepo

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gopi.githubrepo.Fragments.DetailFragment
import com.gopi.githubrepo.Fragments.HomeFragment
import com.gopi.githubrepo.Fragments.MoreFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var bottomNavigationView: BottomNavigationView? = null
    private var navHostFragment: NavHostFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpNavigation()
    }

    private fun setUpNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavView)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        NavigationUI.setupWithNavController(bottomNavigationView!!, navHostFragment!!.navController)

        if (bottomNavigationView != null) {
            bottomNavigationView!!.setOnNavigationItemSelectedListener(this)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {

            R.id.homeMenu -> {
                NavigationUI.onNavDestinationSelected(item, navHostFragment!!.navController)
                return true
            }

            R.id.detailMenu -> {
                NavigationUI.onNavDestinationSelected(item, navHostFragment!!.navController)
                Toast.makeText(applicationContext, "Empty", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.moreMenu -> {
                NavigationUI.onNavDestinationSelected(item, navHostFragment!!.navController)
                Toast.makeText(applicationContext, "Empty", Toast.LENGTH_SHORT).show()
                return true
            }

            else -> return true
        }
    }
}