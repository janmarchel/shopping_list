package com.example.shoppinglist.place;

import android.content.Context;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.Product
import com.example.shoppinglist.ProductClickDeleteInterface
import com.example.shoppinglist.R

class PlaceRVAdapter(
    val context: Context,
    private val placeClickDeleteInterface: PlaceClickDeleteInterface
    ) :
    RecyclerView.Adapter<PlaceRVAdapter.ViewHolder>() {

    // on below line we are creating a
// variable for our all places list.
    private val allPlaces = ArrayList<Place>()

    // on below line we are creating a view holder class.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are creating an initializing all our
        // variables which we have added in layout file.
        val placeNameIV = itemView.findViewById<TextView>(R.id.idTVPlaceName)
        val placeDescIV = itemView.findViewById<TextView>(R.id.idTVPlaceDesc)
        val placeRadiusIV = itemView.findViewById<TextView>(R.id.idTVPlaceRadius)

        val deleteIV = itemView.findViewById<ImageView>(R.id.idIVDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflating our layout file for each item of recycler view.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.place_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.placeNameIV.text = allPlaces[position].name
        holder.placeDescIV.text = allPlaces[position].desc
        holder.placeRadiusIV.text = allPlaces[position].radius.toString()

        holder.deleteIV.setOnClickListener {
            placeClickDeleteInterface.onDeleteIconClick(allPlaces[position])
        }
    }

    override fun getItemCount(): Int {
        return allPlaces.size
    }

    fun updateList(newList: List<Place>) {

        allPlaces.clear()

        allPlaces.addAll(newList)

        notifyDataSetChanged()
    }

}

interface PlaceClickDeleteInterface {

    fun onDeleteIconClick(place: Place)
}
