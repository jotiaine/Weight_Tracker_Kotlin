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

class DailyInputFragment : BaseClassFragment() {
    private lateinit var btnSaveDailyInputFragment: Button
    private lateinit var edtDailyWeight: EditText
    private lateinit var edtDailyCircumference: EditText
    private lateinit var edtDailyBodyFat: EditText

    private fun validateDailyFragmentInputs() {
        when {
            edtDailyWeight.text.toString().isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter your weight",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                saveDailyFragmentInputs(
                    edtDailyWeight,
                    edtDailyCircumference,
                    edtDailyBodyFat
                )
            }
        }
    }

    private fun clearInputs() {
        edtDailyWeight.text.clear()
        edtDailyCircumference.text.clear()
        edtDailyBodyFat.text.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily, container, false)

        btnSaveDailyInputFragment = view.findViewById(R.id.btnSaveDailyInputFragment)
        edtDailyWeight = view.findViewById(R.id.edtDailyWeight)
        edtDailyCircumference = view.findViewById(R.id.edtDailyCircumference)
        edtDailyBodyFat = view.findViewById(R.id.edtDailyBodyFat)

        btnSaveDailyInputFragment.setOnClickListener {
            validateDailyFragmentInputs()
            clearInputs()
        }

        return view
    }
}