package com.example.vinheira_agnello_f4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Produto::class], version = 1, exportSchema = false)
abstract class VinheriaDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao

    companion object {
        @Volatile
        private var INSTANCE: VinheriaDatabase? = null

        fun getInstance(context: Context): VinheriaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VinheriaDatabase::class.java,
                    "vinheria_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

