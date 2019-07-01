package fr.camilo.rockstarsapp.db.dao

import androidx.room.*
import fr.camilo.rockstarsapp.db.entity.RockstarEntity

@Dao
interface RockstarDao {
    @Query("SELECT * from rockstar_table WHERE bookmark = 1 ORDER BY `index` ASC")
    fun getAll(): List<RockstarEntity>

    @Insert
    suspend fun insert(rockstarEntity: RockstarEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rockstarEntityList: ArrayList<RockstarEntity>)

    @Delete
    suspend fun delete(rockstarEntity: RockstarEntity)

    @Update
    suspend fun update(rockstarEntity: RockstarEntity)
}