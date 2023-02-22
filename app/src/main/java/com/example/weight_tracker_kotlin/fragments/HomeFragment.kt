package com.example.weight_tracker_kotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.weight_tracker_kotlin.BaseClassFragment
import com.example.weight_tracker_kotlin.R


class HomeFragment : BaseClassFragment() {
    private lateinit var txvMotivationQuote: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        showFirstInputFragment()
        txvMotivationQuote = view.findViewById(R.id.txvMotivationQuote)

        // Get motivation data
        txvMotivationQuote.text = getMotivationQuote()

        return view
    }

}