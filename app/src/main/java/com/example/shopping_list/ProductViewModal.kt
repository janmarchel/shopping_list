package com.example.shopping_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModal(application: Application) : AndroidViewModel(application) {

    // on below line we are creating a variable
    // for our all Products list and repository
    val allProducts: LiveData<List<Product>>
    val repository: ProductRepository

    // on below line we are initializing
    // our dao, repository and all Products
    init {
        val dao = ProductDatabase.getDatabase(application).getProductDao()
        repository = ProductRepository(dao)
        allProducts = repository.allProducts
    }

    // on below line we are creating a new method for deleting a Product. In this we are
    // calling a delete method from our repository to delete our Product.
    fun deleteProduct(Product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(Product)
    }

    // on below line we are creating a new method for updating a Product. In this we are
    // calling a update method from our repository to update our Product.
    fun updateProduct(Product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(Product)
    }


    // on below line we are creating a new method for adding a new Product to our database
    // we are calling a method from our repository to add a new Product.
    fun addProduct(Product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(Product)
    }
}