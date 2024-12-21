package com.example.panicbutton.viewModel

import android.renderscript.Sampler.Value
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.panicbutton.model.MonitorRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardUserViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val userDatabaseRef = database.getReference("Panic_Button/users")
    private val monitorDatabaseRef = database.getReference("Panic_Button/monitor")
    private val buzzerDatabaseRef = database.getReference("Panic_Button/buzzer")
    private val buzzerPriorityDatabaseRef = database.getReference("Panic_Button/buzzer_priority")

    private val _userName = mutableStateOf("")
    val userName: State<String> get() = _userName

    private val _houseNumber = mutableStateOf("")
    val houseNumber: State<String> get() = _houseNumber

    private val _buzzerState = MutableStateFlow("off")
    val buzzerState: StateFlow<String> get() = _buzzerState

    private var isUserDataLoaded = false

    init {
        // Inisialisasi untuk mendapatkan status awal buzzer dari Firebase
        getBuzzerState()
    }

    private val _userHistory = MutableStateFlow<List<MonitorRecord>>(emptyList())
    val userHistory : StateFlow<List<MonitorRecord>> get() = _userHistory

    private fun getCurrentTimestampFormatted(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd 'waktu' HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    //function menampilkan nama dan nomor rumah user
    fun fetchUserData(uid: String) {
        if (!isUserDataLoaded) { //apakah data sudah dimuat sebelumnya
            userDatabaseRef.child(uid).get()
                .addOnSuccessListener { snapshot ->
                    _userName.value = snapshot.child("name").getValue(String::class.java) ?: "Unknown"
                    _houseNumber.value = snapshot.child("houseNumber").getValue(String::class.java) ?: "Unknown"
                    isUserDataLoaded = true //Tandai data sudah dimuat
                }.addOnFailureListener {
                    _userName.value = "Error name"
                    _houseNumber.value = "Error house number"
                }
        }
    }

    // fun menyimpan data ke RTB field monitor
    fun saveMonitorData(message: String, priority: String, status: String) {
        val data = mapOf(
            "name" to userName.value,
            "houseNumber" to houseNumber.value,
            "message" to message,
            "priority" to priority,
            "status" to status,
            "time" to getCurrentTimestampFormatted() // Waktu saat toggle diaktifkan
        )
        viewModelScope.launch {
            monitorDatabaseRef.push().setValue(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("saveMonitorData", "Data berhasil disimpan ke monitor")
                    } else {
                        Log.e("saveMonitorData", "Gagal menyimpan data", task.exception)
                    }
                }
        }
    }

    // Fungsi untuk mengatur status buzzer di Firebase
    fun setBuzzerState(state: String) {
        buzzerDatabaseRef.setValue(state)
    }

    // Fungsi untuk mendapatkan status buzzer dari Firebase
    private fun getBuzzerState() {
        buzzerDatabaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _buzzerState.value = snapshot.getValue(String::class.java) ?: "Off"
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error, bisa diisi log jika dibutuhkan
            }
        })
    }

    //update status buzzer
    fun updateBuzzerState(isOn: Boolean, priority: String? = null) {
        viewModelScope.launch {
            if (isOn && priority != null) {
                buzzerPriorityDatabaseRef.setValue(priority)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("UpdateBuzzerState", "Buzzer updated to: $priority")
                        } else {
                            Log.e("UpdateBuzzerState", "gagal update data buzzer", task.exception)
                        }
                    }
            } else {
                // Ketika toggle switch dimatikan, set value ke "off"
                buzzerPriorityDatabaseRef.setValue("off")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("UpdateBuzzerState", "reset data buzzer ke off")
                        } else {
                            Log.e("UpdateBuzzerState", "gagal mereset data buzzer", task.exception)
                        }
                    }
            }
        }
    }

    //fun mengambil riwayat user berdasarkan houseNumber
    fun fetchUserHistory() {
        val houseNumber = houseNumber.value
        monitorDatabaseRef.orderByChild("houseNumber").equalTo(houseNumber)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val historyList = mutableListOf<MonitorRecord>()
                    for (dataSnapshot in snapshot.children.reversed()) {
                        val record = dataSnapshot.getValue(MonitorRecord::class.java)
                        record?.let { historyList.add(it) }
                    }
                    _userHistory.value = historyList
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

    }
}