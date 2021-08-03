package com.rushitest.weatherapp.di

import android.app.Application
import android.content.Context

import com.rushitest.weatherapp.features.weather_info_show.view.MainActivity
import com.rushitest.weatherapp.features.weather_info_show.view.RecyclerViewActivity
import com.rushitest.weatherapp.network.ApiInterface
import com.rushitest.weatherapp.network.ApiInterfaceMovie
import com.rushitest.weatherapp.network.RetrofitClient
import com.rushitest.weatherapp.network.RetrofitClientMovie

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    abstract fun provideContext(application: Application): Context

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivityInjector(): MainActivity

    @ContributesAndroidInjector(modules = [RecyclerViewActivityModule::class])
    abstract fun recyclerActivityInjector(): RecyclerViewActivity

    companion object {

        @Provides
        @Singleton
        @JvmStatic
        fun provideApiService() : ApiInterface {
            return RetrofitClient.client.create(ApiInterface::class.java)
        }

        @Provides
        @Singleton
        @JvmStatic
        fun provideApiServiceMovie() : ApiInterfaceMovie {
            return RetrofitClientMovie.client.create(ApiInterfaceMovie::class.java)
        }
    }
}