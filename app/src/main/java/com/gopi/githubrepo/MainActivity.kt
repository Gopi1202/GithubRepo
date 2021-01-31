package com.gopi.githubrepo

import android.os.Bundle
import android.view.MenuItem
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
    private val homeFragment: Fragment = HomeFragment()
    private val detailFragment: Fragment = DetailFragment()
    private val moreFragment: Fragment = MoreFragment()
    private val fragmentManager: FragmentManager = supportFragmentManager
    private var activeFragment: Fragment = homeFragment

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

        /*fragmentManager.beginTransaction().add(R.id.main_container, moreFragment, "3").hide(moreFragment).commit()
        fragmentManager.beginTransaction().add(R.id.main_container, detailFragment, "2").hide(detailFragment).commit()
        fragmentManager.beginTransaction().add(R.id.main_container, homeFragment, "1").commit()*/
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {

            R.id.homeMenu -> {
                NavigationUI.onNavDestinationSelected(item, navHostFragment!!.navController)
                /*fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit()
                activeFragment = homeFragment*/
                return true
            }

            R.id.detailMenu -> {
                NavigationUI.onNavDestinationSelected(item, navHostFragment!!.navController)
                /*fragmentManager.beginTransaction().hide(activeFragment).show(detailFragment).commit()
                activeFragment = detailFragment*/
                return true
            }

            R.id.moreMenu -> {
                NavigationUI.onNavDestinationSelected(item, navHostFragment!!.navController)
                /*fragmentManager.beginTransaction().hide(activeFragment).show(moreFragment).commit()
                activeFragment = moreFragment*/
                return true
            }

            else -> return true
        }
    }
}