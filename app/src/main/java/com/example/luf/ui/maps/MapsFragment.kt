package com.example.luf.ui.maps

import MyLocationListener
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.luf.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MapsFragment : Fragment(),OnMapReadyCallback {

    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var locx: Double? = null
    private var locy: Double? = null
    private lateinit var locationManager: LocationManager
    private lateinit var location: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var userLatLng: LatLng
    private lateinit var locListen:MyLocationListener
    private val options: ArrayList<String> = ArrayList()
    private val mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
    private var query = mFirebaseDatabaseReference.child("Contacts")
    private var queryNewText =  mFirebaseDatabaseReference.child("User")
    private var contacts: ArrayList<String> = ArrayList()
    private var users: MutableMap<String, String>? = mutableMapOf()
    private var spinner: Spinner? = null
    private var googleMap: GoogleMap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_maps, container, false)
        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        return root
    }

    override fun onMapReady(googleMap: GoogleMap)
    {
        locListen=MyLocationListener()
        locListen.SetUpLocationListener(requireContext(), requireActivity())
        if(CheckLoactionPermition())
        {

            lat=locListen.getLocation()!!.latitude
            lon=locListen.getLocation()!!.longitude
            FirebaseDatabase.getInstance().reference.child("User").child(FirebaseAuth.getInstance().currentUser!!.uid).child("currentLocationX").setValue(lat)
            FirebaseDatabase.getInstance().reference.child("User").child(FirebaseAuth.getInstance().currentUser!!.uid).child("currentLocationY").setValue(lon)
            this.googleMap = googleMap
            val position = LatLng(lat, lon)

            googleMap.isIndoorEnabled = true
            val uiSettings = googleMap.uiSettings
            uiSettings.isIndoorLevelPickerEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isMapToolbarEnabled = true
            uiSettings.isCompassEnabled = true
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isScrollGesturesEnabled = true
            uiSettings.isZoomGesturesEnabled = true
            googleMap.addMarker(MarkerOptions().position(position).title("You"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))
        } else
        {
            System.err.println("We have not permissions!")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onResume() {
        super.onResume()
        spinner = Spinner(requireContext())
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (FirebaseAuth.getInstance().currentUser!=null) {
                    if (dataSnapshot.key == "Contacts") {

                        for (ds in dataSnapshot.children) {
                            if (FirebaseAuth.getInstance().currentUser!!.uid == ds.child("idFirstClient").value) {
                                contacts.add(ds.child("idSecondClient").value.toString())
                            } else {
                                contacts.add(ds.child("idFirstClient").value.toString())
                            }
                        }
                    } else if (dataSnapshot.key == "User") {
                        for (ds in dataSnapshot.children) {
                            users!!.put(ds.key.toString(), ds.child("fullName").value.toString())
                        }
                    }
                    if (contacts != null && users!!.size != 0) {
                        for (contact in contacts) {
                            options.add(users!!.getValue(contact)!!)
                        }


                        spinner!!.gravity = Gravity.LEFT
                        var ll: LinearLayout = requireActivity().findViewById(R.id.llContacts)
                        var llParamsContact: LinearLayout = LinearLayout(requireContext())
                        val aa_arrAdapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, options)
                        var sc_switch: SwitchCompat = SwitchCompat(requireContext())
                        val tv_txtSwitch: TextView = TextView(requireContext())
                        var temp: String = ""
                        spinner!!.adapter = aa_arrAdapter
                        spinner!!.onItemSelectedListener = object : OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?,
                                                        itemSelected: View, selectedItemPosition: Int, selectedId: Long) {
                                for (user in users!!) {
                                    if (user.value == options[selectedItemPosition]) {
                                        temp = user.key
                                    }
                                }
                                val x = dataSnapshot.child(temp).child("currentLocationX").value as? Double
                                val y = dataSnapshot.child(temp).child("currentLocationY").value as? Double
                                googleMap!!.addMarker(MarkerOptions().position(LatLng(x!!, y!!)).title("!!"))

                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                        ll.addView(spinner)

                    }
                }
            }




                    override fun onCancelled(databaseError: DatabaseError) {}



        }

        query.addListenerForSingleValueEvent(eventListener)
        queryNewText.addListenerForSingleValueEvent(eventListener)


    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    fun CheckLoactionPermition(): Boolean {
        var state = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            requireContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED)
                state = true
            else {
                ActivityCompat.requestPermissions(
                        requireActivity(), arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ), 1000
                )
            }

        } else {
            state = true
        }
        return state
    }
}


