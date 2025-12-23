package com.kilion.swiftdrop.di

import android.content.Context
import com.kilion.swiftdrop.auth.login.storage.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides singleton instances of data-related classes.
 */
@Module
@InstallIn(SingletonComponent::class) // Dependencies live as long as the application.
object DataModule {

    /**
     * Provides a singleton instance of [TokenManager].
     *
     * This function tells Hilt how to create a TokenManager.
     * Hilt automatically provides the `@ApplicationContext` when this function is called.
     *
     * @param context The application context provided by Hilt.
     * @return A singleton instance of [TokenManager].
     */
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    // You can add other providers here for classes like your Retrofit service,
    // repositories, etc., as your app grows.
}
