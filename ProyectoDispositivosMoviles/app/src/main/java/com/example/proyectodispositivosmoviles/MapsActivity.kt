package com.example.proyectodispositivosmoviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.proyectodispositivosmoviles.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.Marker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private var markerNum: Int = 1

    //Conexion a FireBase
    private val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val referenciaBD : DatabaseReference = database.getReference("proyecto_final")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Ocultar el app bar.
        supportActionBar?.hide()



        // Asignar el código para cada uno de los eventos.
        binding.btnGuardar.setOnClickListener {
            Toast.makeText(this, "Longitude ${this.longitud} \n Latitude ${this.latitud}", Toast.LENGTH_LONG).show()
            referenciaBD.child("coordinates").child(this.markerNum.toString()).setValue("Latitude: " + this.latitud.toString()+" Longitude: "+this.longitud.toString())
        }
        binding.btnBorrar.setOnClickListener {
            Toast.makeText(this, "Borrar BD", Toast.LENGTH_LONG).show()
            referenciaBD.child("coordinates").removeValue()
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap2: GoogleMap) {
        this.googleMap = googleMap2

        // Add a marker in Mexico City and move the camera
        val ciudadMexico = LatLng(19.432608, -99.133209)
        this.googleMap.addMarker(MarkerOptions().position(ciudadMexico).title("Ciudad de México").draggable(true))
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(ciudadMexico))
        this.googleMap.moveCamera(CameraUpdateFactory.zoomTo(14F))


        // val parqueLindavista = LatLng(19.486213,-99.131228)
        // this.googleMap.addMarker(MarkerOptions().position(parqueLindavista).title("Parque Lindavista")
        //     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))



        this.latitud = 19.486213
        this.longitud = -99.131228
        // Modificar propiedades en tiempo de ejecución.
        this.googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        this.googleMap.isTrafficEnabled = true
        this.googleMap.isIndoorEnabled = true
        this.googleMap.setOnMapClickListener(object :GoogleMap.OnMapClickListener {
            override fun onMapClick(latlng :LatLng) {
                val location = LatLng(latlng.latitude,latlng.longitude)
                googleMap.addMarker(MarkerOptions().position(location).title("Marcador personalizado  $markerNum").draggable(true))
                markerNum++
                latitud = latlng.latitude
                longitud = latlng.longitude
            }
        })

        this.googleMap.setOnMarkerDragListener(object :GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker) {
            }

            override fun onMarkerDragEnd(p0: Marker) {
                latitud = p0.position.latitude
                longitud = p0.position.longitude
            }

            override fun onMarkerDragStart(marker: Marker) {

            }
        });


    }
}