package com.agugauchat.cinemaapp.di

import android.content.Context
import androidx.room.Room
import com.agugauchat.cinemaapp.data.database.BookingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val BOOKING_DATABASE_NAME = "booking_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, BookingDatabase::class.java, BOOKING_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideBookingDao(db: BookingDatabase) = db.getBookingDao()
}