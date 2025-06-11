package com.bachnn.timeout.di

import android.content.Context
import androidx.room.Room
import com.bachnn.timeout.data.source.local.AppDatabase
import com.bachnn.timeout.data.source.local.dao.AppInfoDao
import com.bachnn.timeout.utilities.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideMyDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context,AppDatabase::class.java,DATABASE_NAME).allowMainThreadQueries().build()
    }


    @Provides
    @Singleton
    fun provideAppInfoDao(appDatabase: AppDatabase): AppInfoDao {
        return appDatabase.getAppInfoDao()
    }

}