package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [BookingEntity::class, PartnerEntity::class, ReviewEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SpaDatabase : RoomDatabase() {
    abstract fun spaDao(): SpaDao

    companion object {
        @Volatile
        private var INSTANCE: SpaDatabase? = null

        fun getInstance(context: Context): SpaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpaDatabase::class.java,
                    "moonnight_spa.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
