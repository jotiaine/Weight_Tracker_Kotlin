package com.example.weight_tracker_kotlin.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.weight_tracker_kotlin.BaseClassFragment
import com.example.weight_tracker_kotlin.R
import com.example.weight_tracker_kotlin.activities.IntroActivity

class AccountFragment : BaseClassFragment() {
    private lateinit var imbLogOut: ImageButton
    private lateinit var txvDeleteAccount: TextView
    private lateinit var intent: Intent

    private fun navigateToIntroActivity() {
        // Go to intro screen
        intent = Intent(requireContext(), IntroActivity::class.java)
        startActivity(intent)
    }

    private fun showAlertDialog() {
        // Show alert dialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete account")
        builder.setMessage("Are you sure you want to delete your account?")
        builder.setPositiveButton("Yes") { _, _ ->
            // Delete account
            println("Attempting to delete account...")
            showProgressBar()
            deleteUserData()
            when (deleteAccount()) {
                true -> {
                    hideProgressBar()
                    navigateToIntroActivity()
                }
                false -> {
                    hideProgressBar()
                    println("Account not deleted")
                }
            }
        }
        builder.setNegativeButton("No") { _, _ ->
            // Do nothing
            println("Doing nothing...")
        }
        builder.show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        txvDeleteAccount = view.findViewById(R.id.txvDeleteAccount)
        imbLogOut = view.findViewById(R.id.imbLogOut)

        // Delete account
        txvDeleteAccount.setOnClickListener {
            // Delete account
            showAlertDialog()
        }

        // Log out
        imbLogOut.setOnClickListener {
            // Log out
            logOut()
            navigateToIntroActivity()
        }

        // Inflate the layout for this fragment
        return view
    }
}