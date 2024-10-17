package com.example.catastrofe.service.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.catastrofe.service.model.PriorityModel

@Database(entities = [PriorityModel::class], version = 1)
abstract class CatastrofeDatabase : RoomDatabase() {

    abstract fun priorityDAO(): PriorityDAO

    companion object {
        private lateinit var INSTANCE: CatastrofeDatabase

        fun getDatabase(context: Context): CatastrofeDatabase {
            if (!Companion::INSTANCE.isInitialized) {
                synchronized(CatastrofeDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context, CatastrofeDatabase::class.java, "tasksDB")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }

}