package com.example.weatherforecast.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_table")
data class Alert(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int,
    var title: String,
    var location: String,
    val latitude: String,
    var longitude: String,
    var date: Long?,
    var time: Pair<Int, Int>?,
    var type: String
)