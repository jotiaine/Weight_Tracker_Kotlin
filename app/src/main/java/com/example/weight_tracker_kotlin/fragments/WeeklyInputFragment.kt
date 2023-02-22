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

class WeeklyInputFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
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
                saveWeeklyFragmentInputs()
            }
        }
    }

    private fun saveWeeklyFragmentInputs() {
        try {
            fireStore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()

            // validate inputs
            val weight = if (edtWeeklyWeight.text.toString().isNotEmpty()) {
                edtWeeklyWeight.text.toString().toDouble()
            } else {
                0.0
            }

            val circumference = if (edtWeeklyCircumference.text.toString().isNotEmpty()) {
                edtWeeklyCircumference.text.toString().toDouble()
            } else {
                0.0
            }

            val bodyFat = if (edtWeeklyBodyFat.text.toString().isNotEmpty()) {
                edtWeeklyBodyFat.text.toString().toDouble()
            } else {
                0.0
            }

            val UID = auth.currentUser!!.uid

            val userMeasurementsMap = hashMapOf(
                "uid" to UID,
                "weight" to weight,
                "circumference" to circumference,
                "bodyFat" to bodyFat,
                "inputType" to "weekly",
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

                    // hide weeklyInputFragment fade animation
                    view?.findViewById<View>(R.id.weekly_input_fragment_root_layout)?.animate()
                        ?.alpha(0f)?.setDuration(300)?.withEndAction {
                            // remove the fragment from the fragment manager
                            parentFragmentManager.beginTransaction()
                                .remove(this@WeeklyInputFragment)
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
        }

        return view
    }
}