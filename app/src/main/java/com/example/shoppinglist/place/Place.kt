package com.example.shoppinglist.place

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place (

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "x") val x: Double,
    @ColumnInfo(name = "y") val y: Double,
    @ColumnInfo(name = "radius") val radius: Double
    ){
        @PrimaryKey(autoGenerate = true) var id = 0
}