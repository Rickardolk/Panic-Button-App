package com.example.panicbutton.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.panicbutton.model.MonitorRecord
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DataRekapViewModel : ViewModel() {
    private val monitorDatabaseRef = FirebaseDatabase.getInstance().getReference("Panic_Button/monitor")

    private val _monitorData = MutableStateFlow<List<MonitorRecord>>(emptyList())
    val monitorData : StateFlow<List<MonitorRecord>> get() = _monitorData

    fun fetchMonitorData() {
        viewModelScope.launch {
            monitorDatabaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val records = mutableListOf<MonitorRecord>()
                    for (recordSnapshot in snapshot.children.reversed()) {
                        val record = recordSnapshot.getValue(MonitorRecord::class.java)
                        record?.let { records.add(it) }
                    }
                    _monitorData.value = records
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("fetchMonitorData", "Failed to fetch monitor data", error.toException())
                }
            })
        }
    }
}