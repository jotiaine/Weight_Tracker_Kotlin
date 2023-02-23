package com.example.weight_tracker_kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.weight_tracker_kotlin.BaseClass
import com.example.weight_tracker_kotlin.R
import com.example.weight_tracker_kotlin.fragments.AccountFragment
import com.example.weight_tracker_kotlin.fragments.HomeFragment
import com.example.weight_tracker_kotlin.fragments.StatisticFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp

class MainActivity : BaseClass() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var homeFragment: HomeFragment
    private lateinit var statisticFragment: StatisticFragment
    private lateinit var accountFragment: AccountFragment
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        fragmentManager = supportFragmentManager
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Show home fragment when user logins
        if (savedInstanceState == null) {
            homeFragment = HomeFragment()
            fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frHome, homeFragment)
            fragmentTransaction.commit()
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            // Remove all fragments first
            fragmentTransaction = fragmentManager.beginTransaction()
            for (fragment in fragmentManager.fragments) {
                fragmentTransaction.remove(fragment)
            }
            fragmentTransaction.commit()

            when (item.itemId) {
                R.id.home -> {
                    homeFragment = HomeFragment()
                    fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frHome, homeFragment)
                    fragmentTransaction.commit()

                    true
                }
                R.id.stats -> {
                    statisticFragment = StatisticFragment()
                    fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frStatistic, statisticFragment)
                    fragmentTransaction.commit()

                    true
                }
                R.id.account -> {
                    accountFragment = AccountFragment()
                    fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frAccount, accountFragment)
                    fragmentTransaction.commit()

                    true
                }
                else -> false
            }
        }
    }
}
