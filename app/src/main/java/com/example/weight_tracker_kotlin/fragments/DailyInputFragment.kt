package com.example.weight_tracker_kotlin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.weight_tracker_kotlin.BaseClass
import com.example.weight_tracker_kotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DailyInputFragment : Fragment() {
    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
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
                saveDailyFragmentInputs()
            }
        }
    }

    private fun saveDailyFragmentInputs() {
        try {
            fireStore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()

            // validate inputs
            val weight = if (edtDailyWeight.text.toString().isNotEmpty()) {
                edtDailyWeight.text.toString().toDouble()
            } else {
                0.0
            }

            val circumference = if (edtDailyCircumference.text.toString().isNotEmpty()) {
                edtDailyCircumference.text.toString().toDouble()
            } else {
                0.0
            }

            val bodyFat = if (edtDailyBodyFat.text.toString().isNotEmpty()) {
                edtDailyBodyFat.text.toString().toDouble()
            } else {
                0.0
            }

            val UID = auth.currentUser!!.uid

            val userMeasurementsMap = hashMapOf(
                "uid" to UID,
                "weight" to weight,
                "circumference" to circumference,
                "bodyFat" to bodyFat,
                "inputType" to "daily",
                "day" to BaseClass().getCurrentWeekday(),
                "date" to BaseClass().getCurrentTimeStamp()
            )

            // update user data to firestore to userMeasurements collection
            fireStore.collection("userMeasurements")
                .document()
                .set(userMeasurementsMap)
                .addOnSuccessListener {
                    Log.d("Success", "User measurements added")
                    Toast.makeText(
                        requireContext(),
                        "User measurements added",
                        Toast.LENGTH_SHORT
                    ).show()

                    // hide dailyInputFragment fade animation
                    view?.findViewById<View>(R.id.daily_input_fragment_root_layout)?.animate()
                        ?.alpha(0f)?.setDuration(300)?.withEndAction {
                            // remove the fragment from the fragment manager
                            parentFragmentManager.beginTransaction().remove(this@DailyInputFragment)
                                .commit()
                        }
                }
                .addOnFailureListener {
                    Log.e("Error", it.message.toString())
                    Toast.makeText(
                        requireContext(),
                        "Error adding user measurements",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
            Toast.makeText(
                requireContext(),
                "Error updating userMeasurements",
                Toast.LENGTH_SHORT
            ).show()
        } finally {
            clearInputs()
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
        }

        return view
    }
}