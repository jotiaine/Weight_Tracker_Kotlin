package com.example.weight_tracker_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.weight_tracker_kotlin.R
import com.example.weight_tracker_kotlin.fragments.AccountFragment
import com.example.weight_tracker_kotlin.fragments.HomeFragment
import com.example.weight_tracker_kotlin.fragments.StatisticFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseClass() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var frameLayout: FrameLayout
    private lateinit var homeFragment: HomeFragment
    private lateinit var statisticFragment: StatisticFragment
    private lateinit var accountFragment: AccountFragment
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide() // hide action bar
        // full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        frameLayout = findViewById(R.id.frame_layout)
        fragmentManager = supportFragmentManager
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // Already on the home destination
                    homeFragment = HomeFragment()
                    fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frame_layout, homeFragment)
                    fragmentTransaction.commit()
                    true
                }
                R.id.stats -> {
                    statisticFragment = StatisticFragment()
                    fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frame_layout, statisticFragment)
                    fragmentTransaction.commit()
                    true
                }
                R.id.account -> {
                    accountFragment = AccountFragment()
                    fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frame_layout, accountFragment)
                    fragmentTransaction.commit()
                    true
                }
                else -> false
            }
        }
    }
}
