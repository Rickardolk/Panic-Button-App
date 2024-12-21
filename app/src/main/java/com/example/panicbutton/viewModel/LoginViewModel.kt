package com.example.panicbutton.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.panicbutton.model.Preference.saveLoginSession
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val authRef = FirebaseAuth.getInstance()

    fun login(email: String, password: String, navController: NavController, context: Context) {
        viewModelScope.launch {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                //cek email dan password "admin"
                if (email == "admin" && password == "admin") {
                    saveLoginSession(context, email, "admin")
                    navController.navigate("dashboard_admin") {
                        popUpTo("login") {inclusive = true}
                    }
                    Toast.makeText(context, "Login sebagai admin", Toast.LENGTH_SHORT).show()
                } else {
                    //login dengan authentication firebase
                    authRef.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener{ task->
                            if (task.isSuccessful){
                                saveLoginSession(context, email, "user")
                                navController.navigate("dashboard_user") {
                                    popUpTo("login") { inclusive = true}
                                }
                                Toast.makeText(context, "Login berhasil", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "email atau password salah", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener{
                            Log.e("login", "Login error")
                        }
                }
            } else {
                Toast.makeText(context, "Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}