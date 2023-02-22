package com.example.weight_tracker_kotlin

import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.weight_tracker_kotlin.fragments.DailyInputFragment
import com.example.weight_tracker_kotlin.fragments.FirstInputFragment
import com.example.weight_tracker_kotlin.fragments.NoInputsFragment
import com.example.weight_tracker_kotlin.fragments.WeeklyInputFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import okhttp3.OkHttpClient

open class BaseClassFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var firstInputFragment: FirstInputFragment
    private lateinit var dailyInputFragment: DailyInputFragment
    private lateinit var weeklyInputFragment: WeeklyInputFragment
    private lateinit var noInputsFragment: NoInputsFragment
    private lateinit var loadingDialog: AlertDialog

    // RapidAPI
    private lateinit var motivationText: String
    private lateinit var author: String
    private lateinit var client: OkHttpClient
    private lateinit var baseUrl: String
    private lateinit var token: String
    private lateinit var url: String
    private lateinit var apiKey: String
    private lateinit var host: String

    protected fun showFirstInputFragment() {
        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        // if startWeight is 0.0 show firstInputFragment
        // else hide firstInputFragment
        val UID = auth.currentUser!!.uid
        fireStore.collection("userBasicInfo")
            .document(UID)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val startWeight = document.get("startWeight")
                    val day = BaseClass().getCurrentWeekday()
                    if (startWeight == 0.0) {
                        fragmentManager = requireActivity().supportFragmentManager
                        fragmentTransaction = fragmentManager.beginTransaction()
                        firstInputFragment = FirstInputFragment()
                        fragmentTransaction.replace(R.id.frInputFragments, firstInputFragment)
                        fragmentTransaction.commit()
                    } else {
                        // if it is not Sunday show dailyInputFragment
                        // if already exist data on today, hide dailyInputFragment
                        println(day)
                        if (day != "SUNDAY") {
                            showDailyInputFragment()
                        } else {
                            // else it is sunday -> show weeklyInputFragment
                            // if already exist data on this day, hide dailyInputFragment
                            println("going to show weeklyInputFragment")
                            showWeeklyInputFragment()
                        }
                    }
                }
            }
    }

    private fun showDailyInputFragment() {
        try {
            fireStore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()

            val UID = auth.currentUser!!.uid

            // Get latest date from userMeasurements
            // if latest date is today, hide dailyInputFragment
            fireStore.collection("userMeasurements")
                .whereEqualTo("uid", UID)
                .whereEqualTo("inputType", "daily")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {
                        val latestDate = documents.documents[0].get("date").toString()
                        val latestDateDate = latestDate.split(" ")[0]
                        val today = BaseClass().getCurrentTimeStamp()
                        val todayDate = today.split(" ")[0]
                        val inputType = documents.documents[0].get("inputType").toString()
                        println("latestDate: $latestDate")
                        println("latestDateDate: $latestDateDate")
                        println("today: $today")
                        println("todayDate: $todayDate")
                        println(latestDateDate == todayDate)

                        if (latestDateDate.lowercase() == todayDate.lowercase() && inputType == "daily") {
                            // hide dailyInputFragment if showing
                            // show noInputsFragment if not showing
                            println("show noInputsFragment")
                            fragmentManager = requireActivity().supportFragmentManager
                            fragmentTransaction = fragmentManager.beginTransaction()
                            noInputsFragment = NoInputsFragment()
                            fragmentTransaction.replace(R.id.frInputFragments, noInputsFragment)
                            fragmentTransaction.commit()
                        } else {
                            println("show dailyInputFragment")
                            fragmentManager = requireActivity().supportFragmentManager
                            fragmentTransaction = fragmentManager.beginTransaction()
                            dailyInputFragment = DailyInputFragment()
                            fragmentTransaction.replace(R.id.frInputFragments, dailyInputFragment)
                            fragmentTransaction.commit()
                        }
                    } else {
                        println("show dailyInputFragment")
                        fragmentManager = requireActivity().supportFragmentManager
                        fragmentTransaction = fragmentManager.beginTransaction()
                        dailyInputFragment = DailyInputFragment()
                        fragmentTransaction.replace(R.id.frInputFragments, dailyInputFragment)
                        fragmentTransaction.commit()
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Error getting documents: ", it)
                }
        } catch (e: Exception) {
            Log.e("TAG", "showDailyInputFragment: ", e)
        } finally {
            Log.d("TAG", "showDailyInputFragment: finally")
        }
    }

    private fun showWeeklyInputFragment() {
        try {
            fireStore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()

            val UID = auth.currentUser!!.uid

            // Get latest date from userMeasurements
            // if latest date is today, hide weeklyInputFragment
            fireStore.collection("userMeasurements")
                .whereEqualTo("uid", UID)
                .whereEqualTo("inputType", "weekly")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {
                        val latestDate = documents.documents[0].get("date").toString()
                        val latestDateDate = latestDate.split(" ")[0]
                        val today = BaseClass().getCurrentTimeStamp()
                        val todayDate = today.split(" ")[0]
                        val inputType = documents.documents[0].get("inputType").toString()
                        println("latestDate: $latestDate")
                        println("latestDateDate: $latestDateDate")
                        println("today: $today")
                        println("todayDate: $todayDate")
                        println(latestDateDate == todayDate)

                        if (latestDateDate.lowercase() == todayDate.lowercase() && inputType == "weekly") {
                            // hide weeklyInputFragment if showing
                            // show noInputsFragment if not showing
                            println("showNoInputsFragment")
                            fragmentManager = requireActivity().supportFragmentManager
                            fragmentTransaction = fragmentManager.beginTransaction()
                            noInputsFragment = NoInputsFragment()
                            fragmentTransaction.replace(R.id.frInputFragments, noInputsFragment)
                            fragmentTransaction.commit()
                        } else {
                            println("showWeeklyInputFragment")
                            fragmentManager = requireActivity().supportFragmentManager
                            fragmentTransaction = fragmentManager.beginTransaction()
                            weeklyInputFragment = WeeklyInputFragment()
                            fragmentTransaction.replace(R.id.frInputFragments, weeklyInputFragment)
                            fragmentTransaction.commit()
                        }
                    } else {
                        println("showWeeklyInputFragment")
                        fragmentManager = requireActivity().supportFragmentManager
                        fragmentTransaction = fragmentManager.beginTransaction()
                        weeklyInputFragment = WeeklyInputFragment()
                        fragmentTransaction.replace(R.id.frInputFragments, weeklyInputFragment)
                        fragmentTransaction.commit()
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Error getting documents: ", it)
                }
        } catch (e: Exception) {
            Log.e("TAG", "showWeeklyInputFragment: ", e)
        } finally {
            Log.d("TAG", "showWeeklyInputFragment: finally")
        }
    }

    protected fun saveFirstFragmentInputs(
        edtStartWeight: EditText,
        edtTargetWeight: EditText,
        edtHeight: EditText,
        edtStartCircumference: EditText,
        edtStartBMI: EditText,
        edtAge: EditText,
        edtGender: EditText,
        edtStartBodyFat: EditText,
    ) {
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
                            parentFragmentManager.beginTransaction().remove(this).commit()
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
            Log.d("TAG", "saveFirstFragmentInputs: finally")
        }
    }

    protected fun saveDailyFragmentInputs(
        edtDailyWeight: EditText,
        edtDailyCircumference: EditText,
        edtDailyBodyFat: EditText,
    ) {
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
                            parentFragmentManager.beginTransaction().remove(this).commit()
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
            Log.d("TAG", "saveDailyFragmentInputs: finally")
        }
    }


    protected fun saveWeeklyFragmentInputs(
        edtWeeklyWeight: EditText,
        edtWeeklyCircumference: EditText,
        edtWeeklyBodyFat: EditText,
    ) {
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
                                .remove(this).commit()
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
            Log.d("TAG", "saveWeeklyFragmentInputs: finally")
        }
    }

    protected fun deleteUserData() {
        try {
            fireStore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()

            val UID = auth.currentUser!!.uid

            // Delete user data
            fireStore.collection("userBasicInfo").whereEqualTo("uid", UID).get()
                .addOnSuccessListener { snapshot ->
                    // Iterate through the query results and delete each document
                    snapshot.documents.forEach { document ->
                        document.reference.delete()
                            .addOnSuccessListener {
                                // Document deleted
                                println("Document deleted: ${document.id}")
                            }
                            .addOnFailureListener {
                                // Document not deleted
                                println("Document not deleted: ${document.id}")
                            }
                    }
                }
                .addOnFailureListener {
                    // User data not deleted
                    println("userBasicInfo not deleted")
                }

            fireStore.collection("userMeasurements").whereEqualTo("uid", UID).get()
                .addOnSuccessListener { snapshot ->
                    // Iterate through the query results and delete each document
                    snapshot.documents.forEach { document ->
                        document.reference.delete()
                            .addOnSuccessListener {
                                // Document deleted
                                println("Document deleted: ${document.id}")
                            }
                            .addOnFailureListener {
                                // Document not deleted
                                println("Document not deleted: ${document.id}")
                            }
                    }
                }
                .addOnFailureListener {
                    // User data not deleted
                    println("userMeasurements not deleted")
                }
        } catch (e: Exception) {
            // User data not deleted
            println("User data not deleted")
        } finally {
            // User data deleted
            println("User data deleted completed")
        }
    }

    protected fun deleteAccount(): Boolean {
        try {
            auth = FirebaseAuth.getInstance()

            // Delete account
            auth.currentUser?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Account deleted
                    println("Account deleted")
                    Toast.makeText(context, "Account deleted", Toast.LENGTH_SHORT).show()
                    // Go to intro screen
                } else {
                    // Account not deleted
                    println("Account not deleted")
                    Toast.makeText(context, "Account not deleted", Toast.LENGTH_SHORT).show()
                }
            }
            return true
        } catch (e: Exception) {
            // Account not deleted
            println("Account not deleted")
            Toast.makeText(context, "Account not deleted", Toast.LENGTH_SHORT).show()
            return false
        } finally {
            // Account deleted
            println("Account deleted completed")
            auth.signOut()
        }
    }

    protected fun logOut() {
        auth = FirebaseAuth.getInstance()
        // Log out
        auth.signOut()
    }


    protected fun getMotivationQuote(): String {
        try {

//    GET /quote?token=ipworld.info HTTP/1.1
//    X-Rapidapi-Key: dd6792186dmsh76cb74ea31ac68ap1fd6f6jsna1fcd0a2c466
//    X-Rapidapi-Host: quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com
//    Host: quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com


//            client = OkHttpClient()
//            baseUrl = "https://quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com/"
//            host = "quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com"
//            apiKey = "dd6792186dmsh76cb74ea31ac68ap1fd6f6jsna1fcd0a2c466"
//            token = "quote?token=ipworld.info"
//            url = "$baseUrl$token"
//            val request = Request.Builder()
//                .url(url)
//                .get()
//                .addHeader("X-RapidAPI-Key", apiKey)
//                .addHeader("X-RapidAPI-Host", host)
//                .build()
//
//            val response = client.newCall(request).execute()
//            val responseBody = response.body?.string()
//            println(response)
//            println(responseBody)
//            println(response.code)
//            println("api call started")
//            client = OkHttpClient()
//
//            val request = Request.Builder()
//                .url("")
//                .get()
//                .build()
//
//            val response = client.newCall(request).execute()

//            if (response.isSuccessful) {
//                val json = JSONObject(responseBody)
//                motivationText = json.getString("text")
//                author = json.getString("author")
//                println("api call success")
//                println(motivationText)
//                println(author)
//            } else {
//                println("API call failed")
//                println(responseBody)
//            }
            println(motivationText)
            println(author)
        } catch (e: Exception) {
            motivationText = "Loading..."
            author = "Unknown"
            println(e.message)
        } finally {
            println("Quote api call finished")
            println("$motivationText - $author")
        }
        return "$motivationText - $author"
    }

    protected fun showProgressBar() {
        // Show loading from res/layout/loading.xml
        loadingDialog = AlertDialog.Builder(requireContext())
            .setView(R.layout.loading)
            .setCancelable(false)
            .create()

        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog.show()
    }

    protected fun hideProgressBar() {
        loadingDialog.dismiss()
    }
}