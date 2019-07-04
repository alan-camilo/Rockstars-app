package fr.camilo.rockstarsapp.db.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import fr.camilo.rockstarsapp.db.entity.RockstarEntity

@Dao
interface RockstarDao {
    @Query("SELECT * from rockstar_table ORDER BY `index` ASC")
    suspend fun getAll(): List<RockstarEntity>

    @Query("SELECT * from rockstar_table WHERE `bookmark` = 1 ORDER BY `index` ASC")
    suspend fun getAllBookmarks(): List<RockstarEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insert(rockstarEntity: RockstarEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(rockstarEntityList: List<RockstarEntity>)

    @Delete
    suspend fun delete(rockstarEntity: RockstarEntity)

    @Update(onConflict = REPLACE)
    suspend fun update(rockstarEntity: RockstarEntity)
}