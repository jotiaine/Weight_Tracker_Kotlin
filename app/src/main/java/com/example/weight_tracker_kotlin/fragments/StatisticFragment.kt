package com.example.weight_tracker_kotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.weight_tracker_kotlin.BaseClassFragment
import com.example.weight_tracker_kotlin.R

class StatisticFragment : BaseClassFragment() {
    private lateinit var weightCircularProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistic, container, false)

        weightCircularProgressBar = view.findViewById(R.id.weightCircularProgressBar)

        val startWeight = 122.0
        val targetWeight = 79.0
        val currentWeight = 104.0
        val progress = ((currentWeight - startWeight) / (targetWeight - startWeight) * 100).toInt()

        weightCircularProgressBar.progress = progress


        return view
    }
}