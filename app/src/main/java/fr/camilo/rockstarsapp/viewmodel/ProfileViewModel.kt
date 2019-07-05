package fr.camilo.rockstarsapp.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.camilo.rockstarsapp.util.ImageRotation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    val SHARED_PREF = "shared_pref"
    val PREF_PICTURE_URI = "pref_picture_uri"
    val sharedPref = app.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

    private val profilePicture by lazy {
        MutableLiveData<Bitmap>().also {
            val picture_uri = sharedPref.getString(PREF_PICTURE_URI, "")
            if (picture_uri != "") {
                GlobalScope.launch(Dispatchers.IO) {
                    val rotatedBitmap = ImageRotation().rotateBitmap(picture_uri)
                    if (rotatedBitmap != null) {
                        Log.d("PICTURE_DEBUG", "image rotation done")
                        withContext(Dispatchers.Main) { it.value = rotatedBitmap }
                    }
                }
            }
        }
    }

    /**
     * Retrieve the profile picture
     */
    fun getProfilePicture(): LiveData<Bitmap> = profilePicture

    /**
     * Set a new profile picture
     * @param uri
     */
    fun setProfilePicture(uri: String) {
        with(sharedPref.edit()) {
            putString(PREF_PICTURE_URI, uri)
            apply()
        }
        val bitmap = ImageRotation().rotateBitmap(uri)
        if (bitmap != null) profilePicture.value = bitmap
    }
}