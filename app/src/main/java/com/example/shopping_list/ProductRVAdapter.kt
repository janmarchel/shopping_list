package com.example.shopping_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductRVAdapter(

    val context: Context,
    private val productClickDeleteInterface: ProductClickDeleteInterface,
    private val productClickInterface: ProductClickInterface
) :
    RecyclerView.Adapter<ProductRVAdapter.ViewHolder>() {

    // on below line we are creating a
    // variable for our all Products list.
    private val allProducts = ArrayList<Product>()

    // on below line we are creating a view holder class.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are creating an initializing all our
        // variables which we have added in layout file.
        val productNameIV = itemView.findViewById<TextView>(R.id.idTVProductName)
        val productCostIV = itemView.findViewById<TextView>(R.id.idTVProductCost)
        val productCountIV = itemView.findViewById<TextView>(R.id.idTVProductCount)

        val deleteIV = itemView.findViewById<ImageView>(R.id.idIVDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflating our layout file for each item of recycler view.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.product_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productNameIV.text = allProducts[position].name
        holder.productCostIV.text = allProducts[position].cost
        holder.productCountIV.text = allProducts[position].count

        holder.deleteIV.setOnClickListener {
            productClickDeleteInterface.onDeleteIconClick(allProducts[position])
        }
        holder.itemView.setOnClickListener {
            productClickInterface.onProductClick(allProducts[position])
        }
    }

    override fun getItemCount(): Int {
        return allProducts.size
    }

    fun updateList(newList: List<Product>) {

        allProducts.clear()

        allProducts.addAll(newList)

        notifyDataSetChanged()
    }
}

interface ProductClickDeleteInterface {

    fun onDeleteIconClick(Product: Product)
}

interface ProductClickInterface {

    fun onProductClick(Product: Product)
}