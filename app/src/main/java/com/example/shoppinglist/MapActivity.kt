package com.example.shoppinglist

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location

import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.databinding.ActivityMapBinding
import com.example.shoppinglist.place.*
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager


class MapActivity : AppCompatActivity(), PlaceClickDeleteInterface {

    lateinit var viewModal: PlaceViewModal
    lateinit var placeRV: RecyclerView
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var binding: ActivityMapBinding
    private lateinit var locationManager: LocationManager
    private lateinit var geoClient: GeofencingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        placeRV = findViewById(R.id.placesRV)
        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[PlaceViewModal::class.java]
        geoClient = LocationServices.getGeofencingClient(this)
        placeRV.layoutManager = LinearLayoutManager(this)
        val placeRVAdapter = PlaceRVAdapter(this, this)
        placeRV.adapter = placeRVAdapter

        binding.mapView.also {
            it.getMapboxMap().loadStyleUri(
                Style.MAPBOX_STREETS
            ) {

                viewModal.allPlaces.value?.forEach { x -> addAnnotationToMap(x) }
                /*addAnnotationToMap("Warsaw")*/
            } //This will happen on MapLoad
        }

        binding.btAddLocation.setOnClickListener {
            addAnnotationToMapAndSave(
                binding.etName.text.toString(),
                binding.etDesc.text.toString(),
                binding.etRadius.text.toString()
            )
        }


        var permissionsListener: PermissionsListener = object : PermissionsListener {
            override fun onExplanationNeeded(permissionsToExplain: List<String>) {

            }

            override fun onPermissionResult(granted: Boolean) {
                if (granted) {

                    // Permission sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location

                } else {

                    // User denied the permission

                }
            }
        }

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Permission sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location

        } else {
            permissionsManager = PermissionsManager(permissionsListener)
            permissionsManager.requestLocationPermissions(this)
        }

        viewModal.allPlaces.observe(this, Observer { list ->
            list?.let {
                placeRVAdapter.updateList(it)

            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun addAnnotationToMapAndSave(name: String, desc: String, radius: String) {
        val pointAnnotationManager = binding.mapView.annotations.createPointAnnotationManager()
        val marker = BitmapFactory.decodeResource(resources, R.drawable.red_marker)
        val scaledMarker = Bitmap.createScaledBitmap(
            marker,
            (marker.width * 0.3).toInt(),
            (marker.height * 0.3).toInt(),
            true
        )

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
            return
        }
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        if (location != null) {
            val paOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(location.longitude, location.latitude))
                .withIconImage(scaledMarker)
                .withTextAnchor(TextAnchor.TOP)
                .withTextField(name)

            pointAnnotationManager.create(paOptions)
            viewModal.addPlace(
                Place(
                    name,
                    desc,
                    location.longitude,
                    location.latitude,
                    radius.toDouble()
                )
            )

            Toast.makeText(
                this, "added place with name: " + name + " x: " + location.longitude
                        + " y: " + location.latitude, Toast.LENGTH_SHORT
            ).show()

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ), 1
                )
            }
            LocationServices.getFusedLocationProviderClient(this).lastLocation
                .addOnSuccessListener {
                    if (it == null) {
                        Log.e("geofenceApp", "Location is null.")
                    } else {
                        Log.i("geofenceApp", "Location: ${it.latitude}, ${it.longitude}")
                    }
                    addGeo(location, name)
                }
                .addOnFailureListener {
                    Log.e("geofenceApp", "Location error: ${it.message.toString()}")
                }
        } else {
            Toast.makeText(this, "Location is null!", Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("MissingPermission")
    private fun addGeo(location: Location, name: String) {
        val geofence = Geofence.Builder()
            .setCircularRegion(location.latitude, location.longitude, 100F)
            .setExpirationDuration(30 * 60 * 1000)
            .setRequestId("geo$name")
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        val geoRequest = GeofencingRequest.Builder()
            .addGeofence(geofence)
            .build()

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            Intent(this, GeoReceiver::class.java),
            PendingIntent.FLAG_MUTABLE
        )

        geoClient.addGeofences(geoRequest, pendingIntent)
            .addOnSuccessListener {
                Log.i("geofenceApp", "Geofence: ${geofence.requestId}  is added!")
            }
            .addOnFailureListener {
                Log.e(
                    "geofenceApp",
                    it.message.toString()
                ) //ERROR 1004 = missing ACCESS_BACKGROUND_PERMISSION
            }
    }

    private fun addAnnotationToMap(place: Place) {
        val pointAnnotationManager = binding.mapView.annotations.createPointAnnotationManager()
        val marker = BitmapFactory.decodeResource(resources, R.drawable.red_marker)
        val scaledMarker = Bitmap.createScaledBitmap(
            marker,
            (marker.width * 0.3).toInt(),
            (marker.height * 0.3).toInt(),
            true
        )

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
            return
        }

        val paOptions = PointAnnotationOptions()
            .withPoint(Point.fromLngLat(place.x, place.y))
            .withIconImage(scaledMarker)
            .withTextAnchor(TextAnchor.TOP)
            .withTextField(place.name)
        pointAnnotationManager.create(paOptions)

    }

    override fun onDeleteIconClick(place: Place) {
        viewModal.deletePlace(place)
        Toast.makeText(this, "${place.name} Deleted", Toast.LENGTH_LONG).show()
    }
}