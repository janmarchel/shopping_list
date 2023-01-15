package com.example.shoppinglist.place

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.Product
import com.example.shoppinglist.ProductDatabase
import com.example.shoppinglist.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceViewModal(application: Application) : AndroidViewModel(application) {

    val allPlaces: LiveData<List<Place>>
    val repository: PlaceRepository

    init {
        val dao = ProductDatabase.getDatabase(application).getPlaceDao()
        repository = PlaceRepository(dao)
        allPlaces = repository.allPlaces
    }

    fun deletePlace(place: Place) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(place)
    }

    fun updatePlace(place: Place) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(place)
    }

    fun addPlace(place: Place) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(place)
    }
}