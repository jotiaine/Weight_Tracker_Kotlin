package com.example.weight_tracker_kotlin.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.weight_tracker_kotlin.BaseClassFragment
import com.example.weight_tracker_kotlin.R


class StatisticFragment : BaseClassFragment() {
    private lateinit var weightCircularProgressBar: ProgressBar
    private lateinit var txvRemainingWeightStatsNum: TextView
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


    private fun animateProgressBar(progress: Int) {
        val anim = ObjectAnimator.ofInt(weightCircularProgressBar, "progress", 0, progress)
        anim.duration = 1000
        anim.interpolator = AccelerateInterpolator()
        anim.start()
    }

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
        txvRemainingWeightStatsNum = view.findViewById(R.id.txvRemainingWeightStatsNum)
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

            try {
                // show only 1 decimal
                val startWeight = "%.1f".format(userData["startWeight"].toString().toDoubleOrNull())
                val targetWeight =
                    "%.1f".format(userData["targetWeight"].toString().toDoubleOrNull())
                val currentWeight = "%.1f".format(userData["weight"].toString().toDoubleOrNull())
                val lostWeight = "%.1f".format(userData["lostWeight"].toString().toDoubleOrNull())
                val bmi = "%.1f".format(userData["bmi"].toString().toDoubleOrNull())
                val bodyFat = "%.1f".format(userData["bodyFat"].toString().toDoubleOrNull())
                val visceralFat = "%.1f".format(userData["visceralFat"].toString().toDoubleOrNull())
                val loseWeightAmount = "%.1f".format(
                    userData["startWeight"].toString().toDoubleOrNull()?.minus(
                        userData["targetWeight"].toString().toDoubleOrNull()!!
                    )
                )
                val remainingWeight = "%.1f".format(
                    loseWeightAmount.toDoubleOrNull()?.minus(
                        userData["lostWeight"].toString().toDoubleOrNull()!!
                    )
                )

                val progress = calculateProgress(
                    userData["startWeight"].toString().toDouble(),
                    userData["targetWeight"].toString().toDouble(),
                    userData["weight"].toString().toDouble()
                )

                // Set values
                txvStartWeightStatsNum.text = startWeight // start weight
                txvTargetWeightStatsNum.text = targetWeight // target weight
                txvCurrentWeightStatsNum.text = currentWeight // latest weight
                txvRemainingWeightStatsNum.text = remainingWeight // remaining weight
                txvLostWeightStatsNum.text = lostWeight // lost weight
                animateProgressBar(progress)
                weightCircularProgressBar.progress = progress
                txvBMIStatsNum.text = bmi
                txvBodyFatStatsNum.text = bodyFat
                txvVisceralFatStatsNum.text = visceralFat
            } catch (e: Exception) {
                Log.e("Error", "Error: ${e.message}")
            } finally {
                Log.d("StatisticFragment", "finally")
            }
        }




        return view
    }
}