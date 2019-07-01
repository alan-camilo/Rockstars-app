package fr.camilo.rockstarsapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rockstar_table")
data class RockstarEntity(
    @PrimaryKey val _id: String,
    val about: String,
    val index: Int,
    val name: String,
    val picture: String,
    val bookmark: Boolean
)