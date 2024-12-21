package com.example.panicbutton.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

//class register
class RegisterViewModel : ViewModel() {
    private val authRef = FirebaseAuth.getInstance()
    private val databaseRef = FirebaseDatabase.getInstance().getReference("Panic_Button/users")

    fun register(
        name: String,
        houseNumber: String,
        email: String,
        password: String,
        navController: NavController,
        context: Context
    ) {
        viewModelScope.launch {
            if (name.isNotEmpty() && houseNumber.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                //apakah nomor rumah sudah terdaftar
                databaseRef.orderByChild("houseNumber").equalTo(houseNumber).get()
                    .addOnSuccessListener { houseNumberSnapshot ->
                        if (houseNumberSnapshot.exists()) {
                            Toast.makeText(context, "Nomor Rumah sudah terdaftar", Toast.LENGTH_SHORT).show()
                        } else {
                            // apakah email sudah terdaftar
                            databaseRef.orderByChild("email").equalTo(email).get()
                                .addOnSuccessListener { emailSnapshot ->
                                    if (emailSnapshot.exists()) {
                                        Toast.makeText(context, "Email sudah terdaftar", Toast.LENGTH_SHORT).show()
                                    } else {
                                        //jika houseNumber & email belum terdaftar
                                        authRef.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    val userId = authRef.currentUser?.uid
                                                    val userMap = mapOf(
                                                        "name" to name,
                                                        "houseNumber" to houseNumber,
                                                        "email" to email
                                                    )
                                                    userId?.let {
                                                        databaseRef.child(it).setValue(userMap)
                                                            .addOnCompleteListener { dbTask ->
                                                                if (dbTask.isSuccessful) {
                                                                    navController.navigate("login")
                                                                    Toast.makeText(context, "Register berhasil", Toast.LENGTH_SHORT).show()
                                                                } else {
                                                                    Log.e("register", "Gagal menyimpan data ke database")
                                                                }
                                                            }
                                                    }
                                                } else {
                                                    Toast.makeText(context, "Registrasi gagal", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(context, "Terjadi kesalahan, coba lagi nanti", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Terjadi kesalahan, coba lagi nanti", Toast.LENGTH_SHORT).show()
                    }

            } else {
                Toast.makeText(context, "Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

