package fr.camilo.rockstarsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.camilo.rockstarsapp.db.dao.RockstarDao
import fr.camilo.rockstarsapp.db.entity.RockstarEntity

@Database(entities = [RockstarEntity::class], version = 1)
abstract class RockstarDatabase : RoomDatabase() {
    abstract fun rockstarDao(): RockstarDao

    companion object {
        @Volatile
        private var INSTANCE: RockstarDatabase? = null

        fun getDatabase(context: Context): RockstarDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RockstarDatabase::class.java,
                    "rockstar_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}