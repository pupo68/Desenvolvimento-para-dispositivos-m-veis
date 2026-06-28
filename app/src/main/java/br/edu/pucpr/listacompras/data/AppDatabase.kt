package br.edu.pucpr.listacompras.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.edu.pucpr.listacompras.db.ItemCompraDao
import br.edu.pucpr.listacompras.model.ItemCompra

/**
 * Room Database for the Shopping List app
 * Implements singleton pattern as per SDD section 4.1.3
 * Includes migration from v1 (no categoria) to v2 (with categoria column)
 */
@Database(entities = [ItemCompra::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        /** Migration v1→v2: adds 'categoria' column with default 'Outros' */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE item_compra ADD COLUMN categoria TEXT NOT NULL DEFAULT 'Outros'"
                )
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "compras_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun itemCompraDao(): ItemCompraDao
}
