package com.example.shopping_list

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

class AddEditProductActivity : AppCompatActivity() {

    lateinit var productNameEdt: EditText
    lateinit var productCostEdt: EditText
    lateinit var productCountEdt: EditText
    lateinit var productIsBoughtEdt: CheckBox
    lateinit var preferenceManager: AppPreferenceManager


    lateinit var saveBtn: Button

    lateinit var viewModal: ProductViewModal
    var ProductID = -1;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = AppPreferenceManager(this)
        setContentView(R.layout.activity_add_edit_product)
        if (preferenceManager.darkModeState) {
            setTheme(R.style.AppThemeDark)
        } else {
            setTheme(R.style.AppTheme)
        }

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[ProductViewModal::class.java]

        productNameEdt = findViewById(R.id.idEdtProductName)
        productCostEdt = findViewById(R.id.idEdtProductCost)
        productCountEdt = findViewById(R.id.idEdtProductCount)
        productIsBoughtEdt = findViewById(R.id.idEdtProductIsBought)
              // on below line we are setting data to edit text.
      saveBtn = findViewById(R.id.idBtn)

        val productType = intent.getStringExtra("productType")
        if (productType.equals("Edit")) {
            val productName = intent.getStringExtra("productName")
            val productCost = intent.getStringExtra("productCost")
            val productCount = intent.getStringExtra("productCount")



            ProductID = intent.getIntExtra("ProductId", -1)
            saveBtn.setText("Update Product")
            productNameEdt.setText(productName)
            productCostEdt.setText(productCost)
            productCountEdt.setText(productCount)
            productIsBoughtEdt.isChecked

        } else {
            saveBtn.setText("Save Product")
        }

        saveBtn.setOnClickListener {

            val productName = productNameEdt.text.toString()
            val productCost = productCostEdt.text.toString()
            val productCount = productCountEdt.text.toString()
            val productIsBought = productIsBoughtEdt.isChecked


            if (productType.equals("Edit")) {
                if (productName.isNotEmpty() && productCount.isNotEmpty() && productCost.isNotEmpty()) {
                    val updatedProduct =
                        Product(productName, productCost, productCount, productIsBought)
                    updatedProduct.id = ProductID
                    viewModal.updateProduct(updatedProduct)
//                    Toast.makeText(this, "Product Updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (productName.isNotEmpty() && productCount.isNotEmpty() && productCost.isNotEmpty()) {

                    viewModal.addProduct(
                        Product(
                            productName,
                            productCost,
                            productCount,
                            productIsBought
                        )
                    )
//                    Toast.makeText(this, "$productName Added", Toast.LENGTH_LONG).show()
                }
            }

            sendBroadcast(Intent().also{
                it.component = ComponentName("com.example.broadcastreceiver","com.example.broadcastreceiver.MyReceiver")
                it.putExtra("productName", productName)
                it.putExtra("productCost", productCost)
                it.putExtra("productCount", productCount)
                it.putExtra("productIsBought", productIsBought)
            })
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }
}