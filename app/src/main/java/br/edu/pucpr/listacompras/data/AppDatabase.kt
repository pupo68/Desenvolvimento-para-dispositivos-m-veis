package br.edu.pucpr.listacompras.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.pucpr.listacompras.db.ItemCompraDao
import br.edu.pucpr.listacompras.model.ItemCompra

/**
 * Room Database for the Shopping List app
 * Implements singleton pattern as per SDD section 4.1.3
 */
@Database(entities = [ItemCompra::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Companion object for singleton instance
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "compras_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    // Abstract method to get the DAO
    abstract fun itemCompraDao(): ItemCompraDao
}