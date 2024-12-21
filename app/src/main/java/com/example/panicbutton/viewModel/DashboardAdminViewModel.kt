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

class DashboardAdminViewModel : ViewModel() {
    private val monitorDatabaseRef = FirebaseDatabase.getInstance().getReference("Panic_Button/monitor")

    private val _monitorData = MutableStateFlow<List<MonitorRecord>>(emptyList())
    val monitorData : StateFlow<List<MonitorRecord>> get() = _monitorData

    private val _latestRecord = MutableStateFlow(MonitorRecord())
    val latestRecord: StateFlow<MonitorRecord> = _latestRecord

    //fun utk mengambil data 1 data terbaru
    fun fetchMonitorData() {
        viewModelScope.launch {
            monitorDatabaseRef.orderByKey().limitToLast(1)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val data = snapshot.children.first().getValue(MonitorRecord::class.java)
                            data?.let {
                                viewModelScope.launch {
                                    _latestRecord.emit(it)
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
        }
    }

    //function utk mengambil 3 data terbaru
    fun latestMonitorData() {
        viewModelScope.launch {
            monitorDatabaseRef.orderByKey().limitToLast(3) // take 3 data terbaru
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val records = mutableListOf<MonitorRecord>()
                        for (recordSnapshot in snapshot.children.reversed()) {
                            val record = recordSnapshot.getValue(MonitorRecord::class.java)
                            record?.let { records.add(it) }
                        }
                        _monitorData.value = records // take 3 data
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("latestMonitorData", "Failed to fetch monitor data", error.toException())
                    }
                })
        }
    }
}