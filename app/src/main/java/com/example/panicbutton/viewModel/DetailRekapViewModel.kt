package com.example.panicbutton.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.panicbutton.model.MonitorRecord
import com.example.panicbutton.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailRekapViewModel : ViewModel() {
    private val monitorDatabaseRef = FirebaseDatabase.getInstance().getReference("Panic_Button/monitor")
    private val userDatabaseRef = FirebaseDatabase.getInstance().getReference("Panic_Button/users")

    private val _monitorData = MutableStateFlow<List<MonitorRecord>>(emptyList())
    val monitorData : StateFlow<List<MonitorRecord>> get() = _monitorData

    private val _userData = MutableStateFlow<Map<String, String>>(emptyMap())
    val userData: StateFlow<Map<String, String>> get() = _userData

    private val _userImage = MutableStateFlow<User?>(null)
    val userImage: StateFlow<User?> = _userImage

    //fun detail rekap
    fun detailRekap(houseNumber: String) {
        viewModelScope.launch {
            monitorDatabaseRef.orderByChild("houseNumber").equalTo(houseNumber)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val records = snapshot.children.reversed().mapNotNull { recordSnapshot -> //take data
                            recordSnapshot.getValue(MonitorRecord::class.java)?.copy(id = recordSnapshot.key ?: "") //menetapkan id = key
                        }
                        (records.isNotEmpty()) //check apakah records kosong
                        _monitorData.value = records //jika records tdk kosong update _monitorData
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("detailRekap", "gagal mengambil data", error.toException())
                    }
                })
        }
    }

    //fun update status pesan
    fun updateStatus(recordId: String) {
        monitorDatabaseRef.child(recordId).child("status").setValue("Selesai") //update data di status

    }

    //fun utk fetch no hp dan note user
    fun fetchPhoneNumberAndNote(houseNumber: String) {
        viewModelScope.launch {
            userDatabaseRef.orderByChild("houseNumber").equalTo(houseNumber)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val userSnapshot = snapshot.children.first()
                            val phoneNumber = userSnapshot.child("phoneNumber").getValue(String::class.java) ?: ""
                            val note = userSnapshot.child("note").getValue(String::class.java) ?: ""
                            Log.d("fetchPhoneNumberAndNote", "Data user: phoneNumber=$phoneNumber, note=$note")
                            _userData.value = mapOf(
                                "phoneNumber" to phoneNumber,
                                "note" to note
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("fetchPhoneNumberAndNote", "Gagal mengambil data: ${error.message}")
                    }
                })
        }
    }

    //fun utk mengambil foto profile dan foto rumah
    fun fetchUserImage(houseNumber: String) {
        viewModelScope.launch {
            userDatabaseRef.orderByChild("houseNumber").equalTo(houseNumber)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val matchedUser = snapshot.children // matchedUser utk memfilter user
                            .mapNotNull { it.getValue(User::class.java) }
                            .firstOrNull { it.houseNumber == houseNumber }

                        _userImage.value = matchedUser
                    }
                }

                override fun onCancelled(error: DatabaseError) { }
            })
        }
    }
}