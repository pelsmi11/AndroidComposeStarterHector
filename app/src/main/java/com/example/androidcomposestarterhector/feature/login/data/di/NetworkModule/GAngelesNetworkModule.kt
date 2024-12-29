package com.example.androidcomposestarterhector.feature.login.data.di.NetworkModule

import com.example.androidcomposestarterhector.feature.login.data.GAngelesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object GAngelesNetworkModule {

    private const val BASE_URL = "https://angeles-back-sistem-production.up.railway.app"

    @Provides
    fun provideApiService(retrofit: Retrofit): GAngelesApiService =
        retrofit.create(GAngelesApiService::class.java)

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()
}