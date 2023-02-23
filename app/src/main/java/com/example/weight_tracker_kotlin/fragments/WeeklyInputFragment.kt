package com.example.weight_tracker_kotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.weight_tracker_kotlin.BaseClassFragment
import com.example.weight_tracker_kotlin.R

class WeeklyInputFragment : BaseClassFragment() {
    private lateinit var btnSaveWeeklyInputFragment: Button
    private lateinit var edtWeeklyWeight: EditText
    private lateinit var edtWeeklyCircumference: EditText
    private lateinit var edtWeeklyBodyFat: EditText

    private fun validateWeeklyFragmentInputs() {
        when {
            edtWeeklyWeight.text.toString().isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter your weight",
                    Toast.LENGTH_SHORT
                ).show()
            }
            edtWeeklyCircumference.text.toString().isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter your circumference",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                addWeeklyFragmentInputs(
                    edtWeeklyWeight,
                    edtWeeklyCircumference,
                    edtWeeklyBodyFat
                )
            }
        }
    }

    private fun clearInputs() {
        edtWeeklyWeight.text.clear()
        edtWeeklyCircumference.text.clear()
        edtWeeklyBodyFat.text.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weekly, container, false)

        btnSaveWeeklyInputFragment = view.findViewById(R.id.btnSaveWeeklyInputFragment)
        edtWeeklyWeight = view.findViewById(R.id.edtWeeklyWeight)
        edtWeeklyCircumference = view.findViewById(R.id.edtWeeklyCircumference)
        edtWeeklyBodyFat = view.findViewById(R.id.edtWeeklyBodyFat)


        btnSaveWeeklyInputFragment.setOnClickListener {
            validateWeeklyFragmentInputs()
            clearInputs()
        }

        return view
    }
}