package com.absut.nutrivision.data.di

import android.content.Context
import androidx.room.Room
import com.absut.nutrivision.data.local.AppDatabase
import com.absut.nutrivision.data.local.NutritionRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "nutrivisionDB"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNutritionRecordDao(appDatabase: AppDatabase): NutritionRecordDao {
        return appDatabase.nutritionRecordDao()
    }
}