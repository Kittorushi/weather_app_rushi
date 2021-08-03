package com.rushitest.weatherapp.features.weather_info_show.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.core.app.NavUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.rushitest.weatherapp.R
import com.rushitest.weatherapp.features.weather_info_show.adapter.MovieAdapter
import com.rushitest.weatherapp.features.weather_info_show.viewmodel.MovieViewModel
import com.rushitest.weatherapp.features.weather_info_show.viewmodel.WeatherInfoViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class RecyclerViewActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var factory: WeatherInfoViewModelFactory
    private lateinit var viewModel: MovieViewModel
    val adapter = MovieAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        // initialize ViewModel
        viewModel = ViewModelProvider(this, factory).get(MovieViewModel::class.java)


        // initialize Recycler View
        initRv()

        //Fetching Data
        getDefaultData()


        // set LiveData and View click listeners before the call for data fetching
        setLiveDataListeners()


    }

    private fun initRv() {
        findViewById<RecyclerView>(R.id.recyclerview).adapter = adapter
    }


    private fun getDefaultData() {
        viewModel.getMovies()
    }

    private fun setLiveDataListeners() {


        /**
         * Data fetch successfully
         */
        viewModel.movieListLiveData.observe(this, Observer { movie ->
            Log.d("TAG", "setLiveDataListeners:  ${movie[0].name}")

            adapter.setMovieList(movie)
        })

        /**
         * If get any error while fetching data show error
         */
        viewModel.movieListFailLiveData.observe(this, Observer { it ->
            Log.d("TAG", "setLiveDataListeners: eefe ${it}")
        })
    }



}