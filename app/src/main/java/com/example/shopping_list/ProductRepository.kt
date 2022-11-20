package com.example.shopping_list

import androidx.lifecycle.LiveData


class ProductRepository(private val productDao: ProductDao) {

    // on below line we are creating a variable for our list
    // and we are getting all the Products from our DAO class.
    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()

    // on below line we are creating an insert method
    // for adding the Product to our database.
    fun insert(Product: Product) {
        productDao.insert(Product)
    }

    // on below line we are creating a delete method
    // for deleting our Product from database.
    suspend fun delete(Product: Product) {
        productDao.delete(Product)
    }

    // on below line we are creating a update method for
    // updating our Product from database.
    suspend fun update(Product: Product) {
        productDao.update(Product)
    }
}