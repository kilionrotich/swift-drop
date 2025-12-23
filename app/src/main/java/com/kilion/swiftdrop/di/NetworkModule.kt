package com.kilion.swiftdrop.di

import com.google.firebase.firestore.FirebaseFirestore
import com.kilion.swiftdrop.BuildConfig
import com.kilion.swiftdrop.auth.login.network.AuthService
import com.kilion.swiftdrop.auth.login.network.CustomerService
import com.kilion.swiftdrop.auth.login.network.RiderService
import com.kilion.swiftdrop.data.remote.ShopService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return if (BuildConfig.DEBUG) "http://10.0.2.2:5000/" else "https://your-production-url.com/"
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideShopService(retrofit: Retrofit): ShopService {
        return retrofit.create(ShopService::class.java)
    }

    @Provides
    @Singleton
    fun provideCustomerService(retrofit: Retrofit): CustomerService {
        return retrofit.create(CustomerService::class.java)
    }

    @Provides
    @Singleton
    fun provideRiderService(retrofit: Retrofit): RiderService {
        return retrofit.create(RiderService::class.java)
    }
}