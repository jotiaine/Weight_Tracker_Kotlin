package com.example.weight_tracker_kotlin

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
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

    // hashmaps
    private lateinit var userBasicInfoMap: MutableMap<String, Any>

    //    "uid" to UID,
    //    "startWeight" to startWeight,
    //    "targetWeight" to targetWeight,
    //    "height" to height,
    //    "startCircumference" to startCircumference,
    //    "startBMI" to startBMI,
    //    "age" to age,
    //    "gender" to gender,
    //    "startBodyFat" to startBodyfat,
    //    "date" to BaseClass().getCurrentTimeStamp()
    private lateinit var userMeasurementsMap: MutableMap<String, Any>
    //    "uid" to UID,
    //    "weight" to weight,
    //    "circumference" to circumference,
    //    "bodyFat" to bodyFat,
    //    "inputType" to "daily",
    //    "day" to BaseClass().getCurrentWeekday(),
    //    "date" to BaseClass().getCurrentTimeStamp()


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

        // Get data from userBasicInfo
        // if startWeight is 0.0(default, not set) show firstInputFragment
        // else show dailyInputFragment or weeklyInputFragment
        // first login -> firstInputFragment
        // monday to saturday -> dailyInputFragment // if done -> noInputsFragment
        // sunday -> weeklyInputFragment // if done -> noInputsFragment
        val UID = auth.currentUser!!.uid
        fireStore.collection("userBasicInfo")
            .document(UID)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val startWeight = document.get("startWeight")
                    val day = BaseClass().getCurrentWeekday()
                    if (startWeight == 0.0) {
                        showFragment("firstInputFragment")
                    } else {
                        if (day != "SUNDAY") {
                            // monday to saturday
                            showDailyInputFragment()
                        } else {
                            // sunday
                            showWeeklyInputFragment()
                        }
                    }
                } else {
                    Log.d("TAG", "No such document")
                }
            }
    }

    private fun showDailyInputFragment() {
        try {
            fireStore = FirebaseFirestore.getInstance()
            auth = FirebaseAuth.getInstance()

            val UID = auth.currentUser!!.uid

            // Get latest date from userMeasurements
            // if latest date is today, show noInputFragment
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
                            // User has done daily input today
                            // show noInputsFragment
                            println("show noInputsFragment")
                            showFragment("noInputsFragment")
                        } else {
                            // User has not done daily input today
                            // show dailyInputFragment
                            println("show dailyInputFragment")
                            showFragment("dailyInputFragment")
                        }
                    } else {
                        // Not found latest userMeasurements
                        // show dailyInputFragment
                        println("show dailyInputFragment")
                        showFragment("dailyInputFragment")
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
            // if latest date is today, show noInputFragment
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
                            // User has done weekly input today
                            // show noInputsFragment
                            println("showNoInputsFragment")
                            showFragment("noInputsFragment")
                        } else {
                            // User has not done weekly input today
                            // show weeklyInputFragment
                            println("showWeeklyInputFragment")
                            showFragment("weeklyInputFragment")
                        }
                    } else {
                        // Not found latest userMeasurements
                        // show weeklyInputFragment
                        println("showWeeklyInputFragment")
                        showFragment("weeklyInputFragment")
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

    private fun showFragment(fragment: String) {
        try {
            fragmentManager = requireActivity().supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction()
            // if fragment is
            when (fragment) {
                "firstInputFragment" -> {
                    firstInputFragment = FirstInputFragment()
                    fragmentTransaction.replace(R.id.frInputFragments, firstInputFragment)
                    fragmentTransaction.commit()
                }
                "dailyInputFragment" -> {
                    dailyInputFragment = DailyInputFragment()
                    fragmentTransaction.replace(R.id.frInputFragments, dailyInputFragment)
                    fragmentTransaction.commit()
                }
                "weeklyInputFragment" -> {
                    weeklyInputFragment = WeeklyInputFragment()
                    fragmentTransaction.replace(R.id.frInputFragments, weeklyInputFragment)
                    fragmentTransaction.commit()
                }
                "noInputsFragment" -> {
                    noInputsFragment = NoInputsFragment()
                    fragmentTransaction.replace(R.id.frInputFragments, noInputsFragment)
                    fragmentTransaction.commit()
                }
                else -> {
                    // do nothing
                    Log.d("TAG", "showFragment: Else block")
                    return
                }
            }
        } catch (e: Exception) {
            Log.e("Error", "showFragment: ${e.message}")
        }
    }

    protected fun addFirstFragmentInputs(
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

            userBasicInfoMap = hashMapOf(
                "uid" to UID,
                "startWeight" to startWeight,
                "targetWeight" to targetWeight,
                "height" to height,
                "startCircumference" to startCircumference,
                "startBMI" to startBMI,
                "age" to age,
                "gender" to gender,
                "startBodyFat" to startBodyfat,
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
                    showToast("User data saved successfully")
                }
                .addOnFailureListener { e ->
                    Log.w("Error", "Error updating userBasicInfo", e)
                    showToast("Error updating userBasicInfo")
                }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
            showToast("Error updating userBasicInfo")
        } finally {
            Log.d("TAG", "saveFirstFragmentInputs: finally")
        }
    }

    protected fun addDailyFragmentInputs(
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

            userMeasurementsMap = hashMapOf(
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

                    showToast("User measurements added")

                    // hide dailyInputFragment fade animation
                    view?.findViewById<View>(R.id.daily_input_fragment_root_layout)?.animate()
                        ?.alpha(0f)?.setDuration(300)?.withEndAction {
                            // remove the fragment from the fragment manager
                            parentFragmentManager.beginTransaction().remove(this).commit()
                        }
                }
                .addOnFailureListener {
                    Log.e("Error", it.message.toString())

                    showToast("Error adding user measurements")
                }

            // Delete the default userMeasurements input after first daily input
            fireStore.collection("userMeasurements")
                .whereEqualTo("uid", UID)
                .orderBy("date")
                .limit(2)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {
                        // There is at least one document that matches the query
                        val latestDoc = documents.documents[0]
                        val latestDocId = latestDoc.id

                        // Delete the oldest document
                        fireStore.collection("userMeasurements")
                            .document(latestDocId)
                            .delete()
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "Oldest default document from userMeasurements deleted successfully"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w(
                                    TAG,
                                    "Error deleting default document from userMeasurement",
                                    e
                                )
                            }
                    } else {
                        // There are no documents that match the query
                        Log.d(TAG, "No documents found that match the query")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error querying latest userMeasurements documents", e)
                }

        } catch (e: Exception) {
            Log.e("Error", e.message.toString())

            showToast("Error adding user measurements")
        } finally {
            Log.d("TAG", "saveDailyFragmentInputs: finally")
        }
    }

    protected fun addWeeklyFragmentInputs(
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

            userMeasurementsMap = hashMapOf(
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

                    showToast("User measurements added")

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

                    showToast("Error adding user measurements")
                }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())

            showToast("Error adding user measurements")
        } finally {
            Log.d("TAG", "saveWeeklyFragmentInputs: finally")
        }
    }

    protected fun getUserData(
        llHistory: LinearLayout,
        onSuccess: (userMap: HashMap<String, String>) -> Unit
    ) {
        try {
            auth = FirebaseAuth.getInstance()
            fireStore = FirebaseFirestore.getInstance()

            val UID = auth.currentUser!!.uid

            val userMap = hashMapOf<String, String>()

            showProgressBar()
            // get userBasicInfo from firestore
            fireStore.collection("userBasicInfo").whereEqualTo("uid", UID)
                .get()
                .addOnSuccessListener { userData ->
                    val startWeight = userData.documents[0].get("startWeight") ?: "0.0"
                    val targetWeight = userData.documents[0].get("targetWeight") ?: "0.0"
                    val startCircumference =
                        userData.documents[0].get("startCircumference") ?: "0.0"
                    val startBodyFat = userData.documents[0].get("startBodyFat") ?: "0.0"

                    userMap["startWeight"] = startWeight.toString()
                    userMap["targetWeight"] = targetWeight.toString()
                    userMap["startCircumference"] = startCircumference.toString()
                    userMap["startBodyFat"] = startBodyFat.toString()
                    Log.d("Success", "User data fetched")
                    println(userMap)


                    // get userMeasurements from firestore
                    fireStore.collection("userMeasurements")
                        .whereEqualTo("uid", UID)
                        .orderBy("date", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener { userMeasurements ->
                            val inflater = LayoutInflater.from(requireContext())

                            for (i in userMeasurements.documents.size - 1 downTo 0) {
                                val weight = userMeasurements.documents[i].get("weight") ?: "0.0"
                                val date = userMeasurements.documents[i].get("date") ?: "default"

                                val cardView =
                                    inflater.inflate(R.layout.viewcard_history, null, false)
                                cardView.findViewById<TextView>(R.id.txWeightHistory).text =
                                    "$weight kg"
                                cardView.findViewById<TextView>(R.id.txvDateHistory).text =
                                    date.toString()
                                llHistory.addView(cardView)
                            }

                            val weight = when (userMeasurements.documents.last().get("weight")) {
                                0.0 -> startWeight
                                else -> userMeasurements.documents.last().get("weight")
                            }
                            val circumference =
                                userMeasurements.documents.last().get("circumference") ?: "0.0"
                            val bodyFat = userMeasurements.documents.last().get("bodyFat") ?: "0.0"

                            userMap["weight"] = weight.toString()
                            userMap["circumference"] = circumference.toString()
                            userMap["bodyFat"] = bodyFat.toString()
                            userMap["lostWeight"] =
                                (startWeight.toString().toDouble() - weight.toString()
                                    .toDouble()).toString()
                            Log.d("Success", "User measurements fetched")
                            println(userMap)
                            onSuccess(userMap)
                        }
                        .addOnFailureListener { e ->
                            Log.e("Error", "Error getting user measurements: ${e.message}")
                            showToast("Error getting user measurements")
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("Error", "Error getting user data: ${e.message}")
                    showToast("Error getting user data")
                }
        } catch (e: Exception) {
            Log.e("Error", "fetchUserData: ${e.message}")
        } finally {
            Log.d("TAG", "fetchUserData: finally")
            hideProgressBar()
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
                    Log.e("Error", "deleteUserData: ${it.message}")
                }
        } catch (e: Exception) {
            // User data not deleted
            Log.e("Error", "deleteUserData: ${e.message}")
        } finally {
            // User data deleted
            Log.d("TAG", "deleteUserData: finally")
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
                    showToast("Account deleted")
                } else {
                    // Account not deleted
                    println("Account not deleted")
                    showToast("Account not deleted")
                }
            }
            return true
        } catch (e: Exception) {
            // Account not deleted
            Log.e("Error", "deleteAccount: ${e.message}")
            showToast("Account not deleted")
            return false
        } finally {
            // Account deleted
            Log.d("TAG", "deleteAccount: finally")
            auth.signOut()
        }
    }

    protected fun logOut() {
        try {
            auth = FirebaseAuth.getInstance()
            // Log out
            auth.signOut()
        } catch (e: Exception) {
            Log.e("Error", "logOut: ${e.message}")
        } finally {
            Log.d("TAG", "logOut: finally")
        }
    }

    /*******************
     * RapidAPI
     *******************/

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

    /*******************
     * Loading Dialog, Alerts, Toasts & errors
     *******************/

    protected fun showProgressBar() {
        try {
            // Show loading from res/layout/loading.xml
            loadingDialog = AlertDialog.Builder(requireContext())
                .setView(R.layout.loading)
                .setCancelable(false)
                .create()

            loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            loadingDialog.show()
        } catch (e: Exception) {
            Log.e("Error", "showProgressBar: ${e.message}")
        }
    }

    protected fun hideProgressBar() {
        try {
            loadingDialog.dismiss()
        } catch (e: Exception) {
            Log.e("Error", "hideProgressBar: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        try {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("Error", "showToast() error: ${e.message.toString()}")
        }
    }
}