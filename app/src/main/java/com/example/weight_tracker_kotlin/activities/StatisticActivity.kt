package com.example.weight_tracker_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weight_tracker_kotlin.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class StatisticActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)
        println("StatisticActivity")

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    true
                }
                R.id.stats -> {
                    // Already on the stats destination
                    true
                }
                R.id.account -> {
                    true
                }
                else -> false
            }
        }


    }
}