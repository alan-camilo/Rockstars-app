package fr.camilo.rockstarsapp.activity

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import fr.camilo.rockstarsapp.R
import fr.camilo.rockstarsapp.util.ImageRotation
import fr.camilo.rockstarsapp.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.navigator.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {

    val PERMISSION_REQUEST_CODE = 2
    val REQUEST_IMAGE_CAPTURE = 1
    var currentPhotoPath: String = ""
    val PREF_FULL_NAME = "pref_full_name"
    private lateinit var model: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //bottom menu buttons
        bookmarks_btn.setOnClickListener {
            val intent = Intent(this, BookmarksActivity::class.java)
            startActivity(intent)
        }

        rockstars_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        profile_picture.setOnClickListener {
            if (checkPersmission()) takePicture() else requestPermission()
        }

        //check if the profile name exists in the sharedPreferences
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val fullName = sharedPref.getString(PREF_FULL_NAME, "")
        if (fullName != "") name_tv.setText(fullName!!)

        //instantiate ProfileViewModel
        model = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        model.getProfilePicture().observe(this, Observer<Bitmap> {
            Log.d("PICTURE_DEBUG", "observer call²ed")
            if (it != null) profile_picture.setImageBitmap(it)
        })
    }

    /**
     * Start the camera application to take a picture
     */
    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
            this,
            "fr.camilo.rockstarsapp.fileprovider",
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    /**
     * Create a file with a unique name
     */
    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageRotation = ImageRotation()
            val rotatedBitmap = imageRotation.rotateBitmap(currentPhotoPath)
            if (rotatedBitmap != null) {
                Log.d("PROFILE_ACTIVITY", "onActivityResult set profile picture")
                profile_picture.setImageBitmap(rotatedBitmap)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_save -> {
                //save the profile name
                val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString(PREF_FULL_NAME, name_tv.text.toString())
                    apply()
                }
                //save the picture
                model.setProfilePicture(currentPhotoPath)
                //user feedback
                Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            CAMERA
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    takePicture()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> Unit
        }
    }
}
