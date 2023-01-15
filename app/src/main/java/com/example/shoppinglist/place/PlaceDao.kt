package com.example.shoppinglist.place

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(place: Place)

    @Query("DELETE FROM places")
    fun clearPlaces()

    @Query("SELECT * FROM places ORDER BY id")
    fun getAllPlaces(): LiveData<List<Place>>

    @Delete
    suspend fun delete(place: Place)

    @Update
    suspend fun update(place: Place)
}