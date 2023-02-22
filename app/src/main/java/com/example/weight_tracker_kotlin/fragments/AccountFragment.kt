package com.example.weight_tracker_kotlin.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.weight_tracker_kotlin.R
import com.example.weight_tracker_kotlin.activities.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userID: String
    private lateinit var imbLogOut: ImageButton
    private lateinit var txvDeleteAccount: TextView
    private lateinit var intent: Intent

    private fun deleteAccount() {
        // Delete account
        auth.currentUser?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Account deleted
                println("Account deleted")
                Toast.makeText(context, "Account deleted", Toast.LENGTH_SHORT).show()
                // Go to intro screen
                navigateToIntroActivity()
            } else {
                // Account not deleted
                println("Account not deleted")
                Toast.makeText(context, "Account not deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logOut() {
        // Log out
        auth.signOut()
        // Go to intro screen
        navigateToIntroActivity()
    }

    private fun navigateToIntroActivity() {
        // Go to intro screen
        intent = Intent(activity, IntroActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun showAlertDialog() {
        // Show alert dialog
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete account")
        builder.setMessage("Are you sure you want to delete your account?")
        builder.setPositiveButton("Yes") { _, _ ->
            // Delete account
            println("Attempting to delete account...")
            deleteUserData()
            deleteAccount()
        }
        builder.setNegativeButton("No") { _, _ ->
            // Do nothing
            println("Doing nothing...")
        }
        builder.show()
    }

    private fun deleteUserData() {
        try {
            // Delete user data
            db.collection("userBasicInfo").whereEqualTo("uid", userID).get()
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

            db.collection("userMeasurements").whereEqualTo("uid", userID).get()
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
        }   catch (e: Exception) {
            // User data not deleted
            println("User data not deleted")
        } finally {
            // User data deleted
            println("User data deleted completed")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        userID = auth.currentUser?.uid.toString()
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
            }

        // Inflate the layout for this fragment
        return view
    }
}