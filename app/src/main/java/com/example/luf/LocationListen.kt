import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.core.content.ContentProviderCompat.requireContext

internal class MyLocationListener : LocationListener
{

    var myLocation: Location? = null
    var bestLocation: Location? =null
    override fun onLocationChanged(loc: Location)
    {
        myLocation = loc
    }

    fun sus()
    {
        Log.println(Log.INFO,"sus","sas")
    }
    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    fun SetUpLocationListener(context: Context,activity: Activity)
    {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity, arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            ), 1000
            )
        }
        var providers: MutableList<String> = locationManager.getProviders(true)

        for(provider in providers)
        {
            myLocation = locationManager.getLastKnownLocation(provider)
            if(myLocation==null)
                continue
            if(bestLocation==null|| myLocation!!.accuracy< bestLocation!!.accuracy)
                bestLocation=myLocation
        }
//        locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER,
//                    5000, 10f,
//                    this) // здесь можно указать другие более подходящие вам параметры
//        myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        Log.println(Log.INFO,"sus","sas")


    }

    fun getLocation(): Location? {
        return bestLocation
    }


}