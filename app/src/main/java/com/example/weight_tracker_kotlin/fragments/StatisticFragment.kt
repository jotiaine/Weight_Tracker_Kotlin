package com.example.weight_tracker_kotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.weight_tracker_kotlin.BaseClassFragment
import com.example.weight_tracker_kotlin.R


class StatisticFragment : BaseClassFragment() {
    private lateinit var weightCircularProgressBar: ProgressBar
    private lateinit var txvStartWeightStatsNum: TextView
    private lateinit var txvTargetWeightStatsNum: TextView
    private lateinit var txvCurrentWeightStatsNum: TextView
    private lateinit var txvLostWeightStatsNum: TextView
    private lateinit var horizontalProgressBarBMI: ProgressBar
    private lateinit var txvBMIStatsNum: TextView
    private lateinit var horizontalProgressBarBodyFat: ProgressBar
    private lateinit var txvBodyFatStatsNum: TextView
    private lateinit var horizontalProgressBarVisceralFat: ProgressBar
    private lateinit var txvVisceralFatStatsNum: TextView
    private lateinit var imvStartImageStats: ImageView
    private lateinit var imvLatestImageStats: ImageView
    private lateinit var llHistory: LinearLayout


    private fun calculateProgress(
        startWeight: Double,
        targetWeight: Double,
        currentWeight: Double
    ): Int {
        return ((currentWeight - startWeight) / (targetWeight - startWeight) * 100).toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistic, container, false)

        //init vars
        txvStartWeightStatsNum = view.findViewById(R.id.txvStartWeightStatsNum)
        txvTargetWeightStatsNum = view.findViewById(R.id.txvTargetWeightStatsNum)
        txvCurrentWeightStatsNum = view.findViewById(R.id.txvCurrentWeightStatsNum)
        txvLostWeightStatsNum = view.findViewById(R.id.txvLostWeightStatsNum)
        horizontalProgressBarBMI = view.findViewById(R.id.horizontalProgressBarBMI)
        txvBMIStatsNum = view.findViewById(R.id.txvBMIStatsNum)
        horizontalProgressBarBodyFat = view.findViewById(R.id.horizontalProgressBarBodyFat)
        txvBodyFatStatsNum = view.findViewById(R.id.txvBodyFatStatsNum)
        horizontalProgressBarVisceralFat = view.findViewById(R.id.horizontalProgressBarVisceralFat)
        txvVisceralFatStatsNum = view.findViewById(R.id.txvVisceralFatStatsNum)
        imvStartImageStats = view.findViewById(R.id.imvStartImageStats)
        imvLatestImageStats = view.findViewById(R.id.imvLatestImageStats)
        llHistory = view.findViewById(R.id.llHistory)
        weightCircularProgressBar = view.findViewById(R.id.weightCircularProgressBar)

        //
        getUserData(llHistory) { userData ->
            // Set values
            txvStartWeightStatsNum.text = userData["startWeight"].toString() // start weight
            txvTargetWeightStatsNum.text = userData["targetWeight"].toString() // target weight
            txvCurrentWeightStatsNum.text = userData["weight"].toString() // latest weight
            txvLostWeightStatsNum.text = userData["lostWeight"].toString() // lost weight
            weightCircularProgressBar.progress = calculateProgress(
                userData["startWeight"].toString().toDouble(),
                userData["targetWeight"].toString().toDouble(),
                userData["weight"].toString().toDouble()
            )
            txvBMIStatsNum.text = userData["bmi"].toString()
            txvBodyFatStatsNum.text = userData["bodyFat"].toString()
            txvVisceralFatStatsNum.text = userData["visceralFat"].toString()
        }




        return view
    }
}