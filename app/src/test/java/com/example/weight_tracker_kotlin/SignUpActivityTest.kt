package com.example.weight_tracker_kotlin

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weight_tracker_kotlin.activities.SignUpActivity
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpActivityTest {

    @Test
    fun testSignUp() {
        val signUpActivity = SignUpActivity()
        signUpActivity.signUpUsernameText.setText("testuser@example.com")
        signUpActivity.signUpPasswordText.setText("testpassword")
        signUpActivity.signUp()
        assertTrue(signUpActivity.auth.currentUser != null)
    }

}
