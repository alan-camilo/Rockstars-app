package fr.camilo.rockstarsapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.camilo.rockstarsapp.model.Rockstar

@Entity(tableName = "rockstar_table")
data class RockstarEntity(
    @PrimaryKey val _id: String,
    val about: String,
    val index: Int,
    val name: String,
    val picture: String,
    var bookmark: Boolean
) {

    fun toRockstar(): Rockstar = Rockstar(
        this._id,
        this.about,
        this.index,
        this.name,
        this.picture,
        this.bookmark
    )
}