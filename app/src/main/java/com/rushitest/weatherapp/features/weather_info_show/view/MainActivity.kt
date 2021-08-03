package com.rushitest.weatherapp.features.weather_info_show.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.rushitest.weatherapp.R
import com.rushitest.weatherapp.features.weather_info_show.model.data_class.WeatherData
import com.rushitest.weatherapp.features.weather_info_show.viewmodel.WeatherInfoViewModel
import com.rushitest.weatherapp.features.weather_info_show.viewmodel.WeatherInfoViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.android.support.DaggerAppCompatActivity

import java.util.*
import javax.inject.Inject
import kotlin.math.log


class MainActivity : DaggerAppCompatActivity(), MultiplePermissionsListener {

    @Inject
    lateinit var factory: WeatherInfoViewModelFactory
    private lateinit var viewModel: WeatherInfoViewModel
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            applicationContext
        )
    }
    private var cancellationTokenSource = CancellationTokenSource()
    private val permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // requesting user current location
        requestCurrentLocation()


        // initialize ViewModel
        viewModel = ViewModelProvider(this, factory).get(WeatherInfoViewModel::class.java)

        //Fetching Data
        getDefaultData()


        // set LiveData and View click listeners before the call for data fetching
        setLiveDataListeners()
        setViewClickListener()

    }

    private fun getDefaultData() {
        viewModel.getWeatherInfo("Mumbai")
    }

    private fun setViewClickListener() {
        // View Weather button click listener
        findViewById<ImageButton>(R.id.updateLoc).setOnClickListener {
            if (!locationEnabled()) {
                createLocationRequest()
            } else {
                requestCurrentLocation()
            }
        }

        findViewById<ImageButton>(R.id.showMovie).setOnClickListener {
            startActivity(Intent(this, RecyclerViewActivity::class.java))
        }
    }

    private fun setLiveDataListeners() {

        /**
         * Data fetch successfully
         */
        viewModel.weatherInfoLiveData.observe(this, Observer { weatherData ->
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
            saveDataToPref(weatherData.temperature)
            setWeatherInfo(weatherData)


        })

        /**
         * If get any error while fetching data show error
         */
        viewModel.weatherInfoFailureLiveData.observe(this, Observer { errorMessage ->
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            if (errorMessage == "Not Found") {
                findViewById<TextView>(R.id.errorText).text =
                    "No data available for selected location"
            }
        })
    }

    private fun saveDataToPref(temperature: String) {
        /** Store data into shared Preferences to access it into widget
         */
        val preferences = this.getSharedPreferences("WeatherTemp", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("temp", temperature)
        editor.apply()
        editor.commit()
    }

    private fun setWeatherInfo(weatherData: WeatherData) {

        /**
         *  Populating extracted data into our views */
        findViewById<TextView>(R.id.address).text = weatherData.cityAndCountry
        findViewById<TextView>(R.id.updated_at).text = "Updated at: ${weatherData.dateTime}"
        findViewById<TextView>(R.id.status).text =
            weatherData.weatherConditionIconDescription.capitalize()
        findViewById<TextView>(R.id.temp).text = weatherData.temperature + "°C"
        findViewById<TextView>(R.id.temp_min).text = "Min Temp: " + weatherData.tempMin + "°C"
        findViewById<TextView>(R.id.temp_max).text = "Max Temp: " + weatherData.tempMax + "°C"
        findViewById<TextView>(R.id.sunrise).text = weatherData.sunrise
        findViewById<TextView>(R.id.sunset).text = weatherData.sunset
        findViewById<TextView>(R.id.wind).text = weatherData.windspedd
        findViewById<TextView>(R.id.pressure).text = weatherData.humidity
        findViewById<TextView>(R.id.humidity).text = weatherData.humidity


        /**
         *  Views populated, Hiding the loader, Showing the main design
         */
        findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
        findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE


    }

    private fun requestCurrentLocation() {

        /**
         *  Check Fine Manifest.permission
         */
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED && locationEnabled()
        ) {


            val currentLocationTask: Task<Location> = fusedLocationClient.getCurrentLocation(
                PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )

            currentLocationTask.addOnCompleteListener { task: Task<Location> ->
                val result = if (task.isSuccessful) {
                    val result: Location = task.result
                    val geocoder = Geocoder(this, Locale.getDefault())
                    "Location (success): ${result.latitude}, ${result.longitude}"
                    val addresses: List<Address> = geocoder.getFromLocation(
                        result.latitude,
                        result.longitude,
                        1
                    )
                    val cityName = addresses[0].getAddressLine(0)
                    viewModel.getWeatherInfo(cityName)

                } else {
                    val exception = task.exception
                    "Location (failure): $exception"
                }

            }
        } else {
            /**
             *  Checking Permission using Dexter Lib
             */
            Dexter.withActivity(this)
                .withPermissions(permissions)
                .withListener(this)
                .check()

        }

    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        // Dexter Permission check
        Log.d("TAG", "onPermissionsChecked: (Permission Successful)")

    }

    override fun onPermissionRationaleShouldBeShown(
        permissions: MutableList<PermissionRequest>?,
        token: PermissionToken?
    ) {
        // Dexter Permission check
        Log.d("TAG", "onPermissionRationaleShouldBeShown: Permission rejected ")
        token!!.continuePermissionRequest()
    }

    private fun createLocationRequest() {
        /** Open Setting to enable Location
         * */
        if (!locationEnabled()) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.apply {
                flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            }
            startActivity(intent)
        }
    }

    private fun locationEnabled(): Boolean {
        /** Provide is location is enable or disable
         */
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
