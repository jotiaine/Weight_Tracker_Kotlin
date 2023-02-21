package com.example.weight_tracker_kotlin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.weight_tracker_kotlin.R
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


class HomeFragment : Fragment() {
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

//    GET /quote?token=ipworld.info HTTP/1.1
//    X-Rapidapi-Key: dd6792186dmsh76cb74ea31ac68ap1fd6f6jsna1fcd0a2c466
//    X-Rapidapi-Host: quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com
//    Host: quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com

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

    private fun showInputFragment() {
        firstInputFragment = FirstInputFragment()
        dailyInputFragment = DailyInputFragment()
        weeklyInputFragment = WeeklyInputFragment()


        fragmentManager = childFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frInputFragments, firstInputFragment)
        fragmentTransaction.add(R.id.frInputFragments, dailyInputFragment)
        fragmentTransaction.add(R.id.frInputFragments, weeklyInputFragment)
        fragmentTransaction.commit()


        // if startWeight is 0.0 show firstInputFragment
        // else hide firstInputFragment

        // if it is not Sunday show dailyInputFragment
        // if already exist data on today, hide dailyInputFragment

        // else it is sunday -> show weeklyInputFragment
        // if already exist data on this day, hide dailyInputFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        showInputFragment()
        txvMotivationQuote = view.findViewById(R.id.txvMotivationQuote)

        // Get motivation data
        getMotivationQuote()

        return view
    }

}