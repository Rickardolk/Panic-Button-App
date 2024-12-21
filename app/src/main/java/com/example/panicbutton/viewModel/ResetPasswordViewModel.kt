package com.example.panicbutton.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ResetPasswordViewModel : ViewModel() {
    private val authRef = FirebaseAuth.getInstance()

    fun resetPassword(email: String, context: Context, navController: NavController) {
        viewModelScope.launch {
            if (email.isNotEmpty()) {
                authRef.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Periksa email Anda untuk reset password", Toast.LENGTH_SHORT).show()
                            navController.navigate("login")
                        } else {
                            Toast.makeText(context, "Email tidak terdaftar", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener{
                        Log.e("resetPassword", "Reset password error")
                    }
            } else {
                Toast.makeText(context, "Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}