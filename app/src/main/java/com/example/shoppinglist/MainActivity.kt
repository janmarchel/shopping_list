package com.example.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), ProductClickInterface, ProductClickDeleteInterface {

    lateinit var viewModal: ProductViewModal
    lateinit var productsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var settingsFAB: FloatingActionButton
    lateinit var mapFAB: FloatingActionButton
    lateinit var preferenceManager: AppPreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapFAB = findViewById(R.id.idFAB2)
        settingsFAB = findViewById(R.id.idFAB1)
        productsRV = findViewById(R.id.productsRV)
        addFAB = findViewById(R.id.idFAB)

        preferenceManager = AppPreferenceManager(this)

        if (preferenceManager.darkModeState) {
            setTheme(R.style.AppThemeDark)
        } else {
            setTheme(R.style.AppTheme)
        }

        settingsFAB.setOnClickListener {
            val intent = Intent( this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        mapFAB.setOnClickListener {
            val intent = Intent( this@MainActivity, MapActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        productsRV.layoutManager = LinearLayoutManager(this)

        val productRVAdapter = ProductRVAdapter(this, this, this)

        productsRV.adapter = productRVAdapter

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ProductViewModal::class.java]

        viewModal.allProducts.observe(this, Observer { list ->
            list?.let {
                productRVAdapter.updateList(it)
            }
        })

        addFAB.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditProductActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onProductClick(Product: Product) {
        val intent = Intent(this@MainActivity, AddEditProductActivity::class.java)
        intent.putExtra("productType", "Edit")
        intent.putExtra("productName", Product.name)
        intent.putExtra("productCost", Product.cost)
        intent.putExtra("productCount", Product.count)
        intent.putExtra("productId", Product.id)
        intent.putExtra("isBought", Product.isBought)
        startActivity(intent)
        this.finish()
    }

    override fun onDeleteIconClick(Product: Product) {
        viewModal.deleteProduct(Product)
        Toast.makeText(this, "${Product.name} Deleted", Toast.LENGTH_LONG).show()
    }
}