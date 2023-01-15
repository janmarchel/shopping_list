package com.example.shoppinglist

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product)

    @Query("DELETE FROM products")
    fun clearProducts()

    @Query("SELECT * FROM products ORDER BY id")
    fun getAllProducts(): LiveData<List<Product>>

    @Delete
    suspend fun delete(product: Product)

    @Update
    suspend fun update(product: Product)
}