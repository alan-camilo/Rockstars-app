package fr.camilo.rockstarsapp.model

import fr.camilo.rockstarsapp.db.entity.RockstarEntity

data class Rockstar(
    val _id: String,
    val about: String,
    val index: Int,
    val name: String,
    val picture: String,
    var bookmark: Boolean?
) {
    fun toRockstarEntity(): RockstarEntity = RockstarEntity(
        this._id,
        this.about,
        this.index,
        this.name,
        this.picture,
        this.bookmark ?: false
    )
}