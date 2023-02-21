package com.example.weight_tracker_kotlin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.weight_tracker_kotlin.BaseClass
import com.example.weight_tracker_kotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirstInputFragment : Fragment() {
    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var btnSaveFirstInputFragment: Button
    private lateinit var imbAddStartImage: ImageButton
    private lateinit var edtStartWeight: EditText
    private lateinit var edtTargetWeight: EditText
    private lateinit var edtHeight: EditText
    private lateinit var edtStartCircumference: EditText
    private lateinit var edtStartBMI: EditText
    private lateinit var edtAge: EditText
    private lateinit var edtGender: EditText
    private lateinit var edtStartBodyFat: EditText
    private lateinit var loadingDialog: AlertDialog

    private fun uploadImage() {
        println("Uploading image")
    }

    private fun validateFirstFragmentInputs() {
        when {
            edtStartWeight.text.toString().isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter your start weight",
                    Toast.LENGTH_SHORT
                ).show()
            }

            edtTargetWeight.text.toString().isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter your target weight",
                    Toast.LENGTH_SHORT
                ).show()
            }

            edtHeight.text.toString().isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your height", Toast.LENGTH_SHORT)
                    .show()
            }

            edtStartCircumference.text.toString().isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter your start circumference",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                calculateBMI()
                saveFirstFragmentInputs()
            }
        }
    }

    private fun calculateBMI() {
        try {
            val weight = edtStartWeight.text.toString().toDouble()
            val height = edtHeight.text.toString().toDouble()
            val bmi = weight / (height / 100 * height / 100)
            val bmiRounded = String.format("%.2f", bmi).toDouble()

            if (!(bmiRounded > 10 && bmiRounded < 100)) {
                edtStartBMI.setText("0.0")
            } else {
                edtStartBMI.setText(bmiRounded.toString())
            }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

    private fun saveFirstFragmentInputs() {
        try {
            fireStore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()

            // validate inputs
            val startWeight = if (edtStartWeight.text.toString().isNotEmpty()) {
                edtStartWeight.text.toString().toDouble()
            } else {
                0.0
            }

            val targetWeight = if (edtTargetWeight.text.toString().isNotEmpty()) {
                edtTargetWeight.text.toString().toDouble()
            } else {
                0.0
            }
            val height = if (edtHeight.text.toString().isNotEmpty()) {
                edtHeight.text.toString().toDouble()
            } else {
                0.0
            }
            val startCircumference = if (edtStartCircumference.text.toString().isNotEmpty()) {
                edtStartCircumference.text.toString().toDouble()
            } else {
                0.0
            }
            val startBMI = if (edtStartBMI.text.toString().isNotEmpty()) {
                edtStartBMI.text.toString().toDouble()
            } else {
                0.0
            }
            val age = if (edtAge.text.toString().isNotEmpty()) {
                edtAge.text.toString().toInt()
            } else {
                0
            }
            val gender = edtGender.text.toString().ifEmpty {
                ""
            }
            val startBodyfat = if (edtStartBodyFat.text.toString().isNotEmpty()) {
                edtStartBodyFat.text.toString().toDouble()
            } else {
                0.0
            }

            val UID = auth.currentUser!!.uid

            val userBasicInfoMap = hashMapOf(
                "uid" to UID,
                "startWeight" to startWeight,
                "targetWeight" to targetWeight,
                "height" to height,
                "startCircumference" to startCircumference,
                "startBMI" to startBMI,
                "age" to age,
                "gender" to gender,
                "startBodyfat" to startBodyfat,
                "date" to BaseClass().getCurrentTimeStamp()
            )

            // update user data to firestore to userBasicInfo collection
            fireStore.collection("userBasicInfo")
                .document(UID)
                .set(userBasicInfoMap)
                .addOnSuccessListener {
                    Log.d("Success", "userBasicInfo document successfully updated!")
                    // hide firstInputFragment fade animation
                    view?.findViewById<View>(R.id.first_input_fragment_root_layout)?.animate()
                        ?.alpha(0f)?.setDuration(300)?.withEndAction {
                        // remove the fragment from the fragment manager
                        parentFragmentManager.beginTransaction().remove(this@FirstInputFragment)
                            .commit()
                    }
                    Toast.makeText(
                        requireContext(),
                        "Successfully updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Log.w("Error", "Error updating userBasicInfo", e)
                    Toast.makeText(
                        requireContext(),
                        "Error updating userBasicInfo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
            Toast.makeText(
                requireContext(),
                "Error updating userBasicInfo",
                Toast.LENGTH_SHORT
            ).show()
        } finally {
            clearInputs()
        }
    }

    private fun clearInputs() {
        edtStartWeight.text.clear()
        edtTargetWeight.text.clear()
        edtHeight.text.clear()
        edtStartCircumference.text.clear()
        edtStartBMI.text.clear()
        edtAge.text.clear()
        edtGender.text.clear()
        edtStartBodyFat.text.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_input, container, false)

        // Buttons
        btnSaveFirstInputFragment = view.findViewById(R.id.btnSaveFirstInputFragment)
        imbAddStartImage = view.findViewById(R.id.imbAddStartImage)

        // input fields
        edtTargetWeight = view.findViewById(R.id.edtTargetWeight)
        edtStartCircumference = view.findViewById(R.id.edtStartCircumference)
        edtStartBMI = view.findViewById(R.id.edtStartBMI)
        edtAge = view.findViewById(R.id.edtAge)
        edtGender = view.findViewById(R.id.edtGender)
        edtStartBodyFat = view.findViewById(R.id.edtStartBodyFat)

        // Listen for edtStartWeight changes and calculate BMI
        edtStartWeight = view.findViewById(R.id.edtStartWeight)
        edtHeight = view.findViewById(R.id.edtHeight)

        edtHeight.addTextChangedListener {
            try {
                if (
                    edtStartWeight.text.toString().length > 1 && edtHeight.text.toString().length > 1
                    && edtStartWeight.text.toString().length < 6 && edtHeight.text.toString().length < 6
                ) {
                    calculateBMI()
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }

        btnSaveFirstInputFragment.setOnClickListener {
            try {
                validateFirstFragmentInputs()
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }

        imbAddStartImage.setOnClickListener {
            try {
                uploadImage()
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }

        return view
    }

}