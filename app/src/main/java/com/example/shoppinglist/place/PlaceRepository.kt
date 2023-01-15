package com.example.shoppinglist.place

import androidx.lifecycle.LiveData

class PlaceRepository(private val placeDao: PlaceDao) {

    val allPlaces: LiveData<List<Place>> = placeDao.getAllPlaces()

    fun insert(place: Place) {
        placeDao.insert(place)
    }

    suspend fun delete(place: Place) {
        placeDao.delete(place)
    }

    suspend fun update(place: Place) {
        placeDao.update(place)
    }
}