package com.example.weight_tracker_kotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weight_tracker_kotlin.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("MainActivity")

        bottomNavigationView = findViewById(R.id.bottomNavigationView)


        bottomNavigationView.setOnItemSelectedListener  { item ->
            when (item.itemId) {
                R.id.home -> {
                    // Already on the home destination
                    true
                }
                R.id.stats -> {
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
