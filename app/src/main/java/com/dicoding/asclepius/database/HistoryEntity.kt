package com.dicoding.asclepius.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "predict_history")
@Parcelize
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @field:ColumnInfo("id")
    val id : Int = 0,

    @field:ColumnInfo(name = "image")
    val image : String,
    @field:ColumnInfo(name = "result")
    val result : String,
    @field:ColumnInfo(name = "confidenceScore")
    val confidenceScore : String,
    @field:ColumnInfo(name = "isSuccess")
    var isSuccess : Boolean
) : Parcelable
