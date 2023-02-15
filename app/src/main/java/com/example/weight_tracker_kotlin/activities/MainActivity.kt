package com.example.weight_tracker_kotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weight_tracker_kotlin.R
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}