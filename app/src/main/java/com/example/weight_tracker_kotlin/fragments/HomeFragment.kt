package com.example.weight_tracker_kotlin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.weight_tracker_kotlin.BaseClass
import com.example.weight_tracker_kotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import okhttp3.OkHttpClient


class HomeFragment : Fragment() {
    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var client: OkHttpClient
    private lateinit var baseUrl: String
    private lateinit var token: String
    private lateinit var url: String
    private lateinit var apiKey: String
    private lateinit var host: String
    private lateinit var txvMotivationQuote: TextView
    private lateinit var motivationText: String
    private lateinit var author: String

    // input fragments
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var firstInputFragment: FirstInputFragment
    private lateinit var dailyInputFragment: DailyInputFragment
    private lateinit var weeklyInputFragment: WeeklyInputFragment
    private lateinit var noInputsFragment: NoInputsFragment

//    GET /quote?token=ipworld.info HTTP/1.1
//    X-Rapidapi-Key: dd6792186dmsh76cb74ea31ac68ap1fd6f6jsna1fcd0a2c466
//    X-Rapidapi-Host: quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com
//    Host: quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com

    private fun showFirstInputFragment() {
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
                        if (day != "WEDNESDAY") {
                            showDailyInputFragment()
                        } else {
                            // else it is sunday -> show weeklyInputFragment
                            // if already exist data on this day, hide dailyInputFragment
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
                .orderBy("date")
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {
                        val latestDate = documents.documents[0].get("date").toString()
                        val latestDateDate = latestDate.split(" ")[0]
                        val today = BaseClass().getCurrentTimeStamp()
                        val todayDate = today.split(" ")[0]
                        println("latestDate: $latestDate")
                        println("latestDateDate: $latestDateDate")
                        println("today: $today")
                        println("todayDate: $todayDate")
                        println(latestDateDate == todayDate)

                        if (latestDateDate == todayDate) {
                            // hide dailyInputFragment if showing
                            // show noInputsFragment if not showing
                            fragmentManager = requireActivity().supportFragmentManager
                            fragmentTransaction = fragmentManager.beginTransaction()
                            noInputsFragment = NoInputsFragment()
                            fragmentTransaction.replace(R.id.frInputFragments, noInputsFragment)
                            fragmentTransaction.commit()
                        } else {
                            fragmentManager = requireActivity().supportFragmentManager
                            fragmentTransaction = fragmentManager.beginTransaction()
                            dailyInputFragment = DailyInputFragment()
                            fragmentTransaction.replace(R.id.frInputFragments, dailyInputFragment)
                            fragmentTransaction.commit()
                        }
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
                .orderBy("date")
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {
                        val latestDate = documents.documents[0].get("date").toString()
                        val latestDateDate = latestDate.split(" ")[0]
                        val today = BaseClass().getCurrentTimeStamp()
                        val todayDate = today.split(" ")[0]
                        println("latestDate: $latestDate")
                        println("latestDateDate: $latestDateDate")
                        println("today: $today")
                        println("todayDate: $todayDate")
                        println(latestDateDate == todayDate)

                        if (latestDateDate == todayDate) {
                            // hide dailyInputFragment if showing
                            // show noInputsFragment if not showing
                            fragmentManager = requireActivity().supportFragmentManager
                            fragmentTransaction = fragmentManager.beginTransaction()
                            noInputsFragment = NoInputsFragment()
                            fragmentTransaction.replace(R.id.frInputFragments, noInputsFragment)
                            fragmentTransaction.commit()
                        } else {
                            fragmentManager = requireActivity().supportFragmentManager
                            fragmentTransaction = fragmentManager.beginTransaction()
                            dailyInputFragment = DailyInputFragment()
                            fragmentTransaction.replace(R.id.frInputFragments, weeklyInputFragment)
                            fragmentTransaction.commit()
                        }
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

    private fun getMotivationQuote() {
        try {
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
            txvMotivationQuote.text = "$motivationText - $author"
            println(txvMotivationQuote.text)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        showFirstInputFragment()
        txvMotivationQuote = view.findViewById(R.id.txvMotivationQuote)

        // Get motivation data
        getMotivationQuote()

        return view
    }

}