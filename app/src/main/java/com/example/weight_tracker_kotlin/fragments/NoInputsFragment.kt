package com.example.weight_tracker_kotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weight_tracker_kotlin.BaseClassFragment
import com.example.weight_tracker_kotlin.R

class NoInputsFragment : BaseClassFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_inputs, container, false)
    }
}