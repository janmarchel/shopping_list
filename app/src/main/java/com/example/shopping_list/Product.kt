package com.example.shopping_list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "cost") val cost: String,
    @ColumnInfo(name = "count") val count: String,
    @ColumnInfo(name = "is_bought") val isBought: Boolean
    ){
    @PrimaryKey(autoGenerate = true) var id = 0
}
