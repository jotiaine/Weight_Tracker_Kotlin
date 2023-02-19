package com.example.weight_tracker_kotlin.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.weight_tracker_kotlin.R
import com.example.weight_tracker_kotlin.activities.IntroActivity
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var imbLogOut: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        auth = FirebaseAuth.getInstance()
        imbLogOut = view.findViewById(R.id.imbLogOut)

        imbLogOut.setOnClickListener {
                // Log out
                auth.signOut()
                // Go to intro screen
                val intent = Intent(activity, IntroActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }

        // Inflate the layout for this fragment
        return view
    }
}