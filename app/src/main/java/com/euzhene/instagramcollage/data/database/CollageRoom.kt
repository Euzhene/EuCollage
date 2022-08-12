package com.euzhene.instagramcollage.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ImagePath::class], version = 1, exportSchema = false)
abstract class CollageRoom : RoomDatabase() {
    abstract fun dao(): CollageDao

    companion object {
        private var INSTANCE: CollageRoom? = null
        private const val NAME = "collage_db"
        private val LOCK = Any()
        fun getInstance(context: Context): CollageRoom {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }

                val db = Room.databaseBuilder(context, CollageRoom::class.java, NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = db
                return db
            }

        }
    }
}