package com.example.panicbutton.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.panicbutton.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    private val userDatabaseRef = FirebaseDatabase.getInstance().getReference("Panic_Button/users")
    private val storage = FirebaseStorage.getInstance().reference

    private val _userData = MutableStateFlow<Map<String, String>>(emptyMap())
    val userData: StateFlow<Map<String, String>> get() = _userData

    private val _userImage = MutableStateFlow<User?>(null)
    val userImage: StateFlow<User?> = _userImage

    private var isUserImageLoaded = false

    fun updatePhoneNumber(phoneNumber: String) {
        val currentData = _userData.value
        _userData.value = currentData + ("phoneNumber" to phoneNumber)
    }

    fun updateNote(note: String) {
        val currentData = _userData.value
        _userData.value = currentData + ("note" to note)
    }

    // fun utk save no hp & note user
    fun savePhoneNumberAndNote(houseNumber: String, phoneNumber: String, note: String, context: Context) {
        viewModelScope.launch {
            userDatabaseRef.orderByChild("houseNumber").equalTo(houseNumber)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (child in snapshot.children) {
                                child.ref.child("phoneNumber").setValue(phoneNumber)
                                child.ref.child("note").setValue(note)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.d("savePhoneNumberAndNote", "Data berhasil diperbarui untuk $houseNumber")
                                            Toast.makeText(context, "Keterangan berhasil simpan", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Log.e("savePhoneNumberAndNote", "Gagal memperbarui data: ${task.exception?.message}")
                                        }
                                    }
                            }
                        } else {
                            Log.e("savePhoneNumberAndNote", "Data dengan houseNumber $houseNumber tidak ditemukan")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("savePhoneNumberAndNote", "Error: ${error.message}")
                    }
                })
        }
    }

    // fungsi utk mengambil data dari Firebase
    fun fetchUserData(uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            userDatabaseRef.child(uid).get().addOnSuccessListener { snapshot ->
                val userDataMap = snapshot.value as? Map<String, String> ?: emptyMap()
                _userData.value = userDataMap
            }.addOnFailureListener { exception ->
                Log.e("fetchUserData", "Gagal mengambil data: ${exception.message}")
            }
        }
    }

    //fun upload foto ke storage
    fun uploadImage(imageUri: Uri, houseNumber: String, imageType: String, context: Context) {
        val imageRef = storage.child("panicButton/${imageType}/$houseNumber.jpg")
        viewModelScope.launch {
            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        saveImagePathToDatabase(uri.toString(), houseNumber, imageType, context)
                    }
                }
        }
    }

    //funtion utk simpan path foto ke database
    private fun saveImagePathToDatabase(imageUri: String, houseNumber: String, imageType: String, context: Context) {
        viewModelScope.launch {
            userDatabaseRef.orderByChild("houseNumber").equalTo(houseNumber)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (userSnapshot in snapshot.children) {
                                userSnapshot.ref.child(imageType).setValue(imageUri)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Tunggu beberapa saat", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) { }
                })
        }
    }

    //fun utk mengambil foto profile dan foto rumah
    fun fetchUserImage(houseNumber: String) {
        if (!isUserImageLoaded) {
            userDatabaseRef.orderByChild("houseNumber").equalTo(houseNumber)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val matchedUser = snapshot.children // matchedUser utk memfilter user
                                .mapNotNull { it.getValue(User::class.java) }
                                .firstOrNull { it.houseNumber == houseNumber }

                            _userImage.value = matchedUser
                            isUserImageLoaded = true
                        }
                    }

                    override fun onCancelled(error: DatabaseError) { }
                })
        }
    }
}