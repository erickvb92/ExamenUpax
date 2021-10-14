package com.upax.aplicationupax.vista.movies.mapa_fragment
import android.Manifest
import android.R.attr
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.upax.aplicationupax.R
import com.upax.aplicationupax.vista.movies.base.BaseFragment
import kotlinx.android.synthetic.main.mapa.*
import com.google.firebase.firestore.FirebaseFirestore
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.firebase.firestore.DocumentReference

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.QueryDocumentSnapshot

import com.google.firebase.firestore.QuerySnapshot

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.maps.model.PolylineOptions
import android.R.attr.path
import android.graphics.Color


class MapaFragment : BaseFragment(), OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    internal lateinit var mLastLocation: Location
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: MapaViewModel
    private lateinit var viewFragment: View

    var latitud = ""
    var longitud = ""

    companion object {
        fun newInstance() = MapaFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewFragment = inflater.inflate(R.layout.mapa, container, false)

        return viewFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this)[MapaViewModel::class.java]

        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && activity?.let {
                ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED) {
            ////////////////permisos
            if (activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } == true) {
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                    )
                }
            } else {
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                    )
                }
            }
        } else {
            //carga mapa
            createMapFragment()
            //auto.setText("")
        }

        setObservers()
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            createMapFragment()
        } else {
            Toast.makeText(
                activity,
                "Es necesario activar los permisos",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun createMapFragment() {
        val fragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        fragment!!.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } == PackageManager.PERMISSION_GRANTED) {
                mMap = googleMap

                val ubicacion = activity?.let { getLocationWithCheckNetworkAndGPS(it) }
                if (ubicacion != null) {
                    val loc = LatLng(ubicacion.latitude, ubicacion.longitude)
                    mCurrLocationMarker =
                        mMap.addMarker(MarkerOptions().position(loc).title("Mi ubicacion"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))

                    latitud = ubicacion.latitude.toString()
                    longitud = ubicacion.longitude.toString()

                    try {
                        val db = FirebaseFirestore.getInstance()
// Create a new user with a first, middle, and last name

                        // Create a new user with a first, middle, and last name
                        val user: MutableMap<String, Any> = HashMap()
                        user["latitud"] = latitud
                        user["longitud"] = longitud
                        user["fecha"] = "2021-09-12"
                        user["hora"] = "10:50"

// Add a new document with a generated ID

// Add a new document with a generated ID
                        db.collection("users")
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot added with ID: " + documentReference.id
                                )
                            }
                            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }

                        db.collection("users")
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val points: ArrayList<LatLng> = ArrayList()
                                    val lineOptions = PolylineOptions()

                                    for (document in task.result) {
                                        Log.d(TAG, document.id + " => " + document.data)

                                        var lat = document.getString("latitud")
                                        val lat_double: Double? = lat!!.toDouble()
                                        var lon = document.getString("longitud")
                                        val lon_double: Double? = lon!!.toDouble()

                                        val position = LatLng(lat_double!!, lon_double!!)

                                        points.add(position)
                                    }
                                    lineOptions.addAll(points);
                                    lineOptions.width(12F);
                                    lineOptions.color(Color.RED);

                                    // Drawing polyline in the Google Map for the entire route
                                    mMap.addPolyline(lineOptions);
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.exception)
                                }
                            }


                    } catch (e: Exception) {
                       e.printStackTrace()
                    }
                }

                buildGoogleApiClient()
                mMap.isMyLocationEnabled = true
            }
        } else {
            buildGoogleApiClient()
            mMap.isMyLocationEnabled = true
        }
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onConnected(bundle: Bundle?) {
       if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(activity)
        }
    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onLocationChanged(location: Location) {

        mLastLocation = location
        location.reset()
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }
        //Place current location marker
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Ubicacion")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mMap.addMarker(markerOptions)

        //move map camera
        mMap.clear()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11f))

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(activity)
        }

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    fun getLocationWithCheckNetworkAndGPS(mContext: Context): Location? {
        val lm = (mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        val isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkLocationEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        var networkLoacation: Location? = null
        var gpsLocation: Location? = null
        var finalLoc: Location? = null
        if (isGpsEnabled) if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }
        gpsLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (isNetworkLocationEnabled) networkLoacation =
            lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (gpsLocation != null && networkLoacation != null) {

            //smaller the number more accurate result will
            return if (gpsLocation.accuracy > networkLoacation.accuracy) networkLoacation.also {
                finalLoc = it
            } else gpsLocation.also { finalLoc = it }
        } else {
            if (gpsLocation != null) {
                return gpsLocation.also { finalLoc = it }
            } else if (networkLoacation != null) {
                return networkLoacation.also { finalLoc = it }
            }
        }
        return finalLoc
    }

    private fun setObservers() {


    }

}