package fr.camilo.rockstarsapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.exifinterface.media.ExifInterface

class ImageRotation {

    /**
     * Rotate bitmap to make it at the place
     */
    fun rotateBitmap(uri: String): Bitmap? {
        val bitmap: Bitmap? = BitmapFactory.decodeFile(uri)
        Log.d("PROFILE_ACTIVITY", "$uri")

        val ei = ExifInterface(uri)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        var rotatedBitmap: Bitmap? = null
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 ->
                rotatedBitmap = rotateImage(bitmap!!, 90F)

            ExifInterface.ORIENTATION_ROTATE_180 ->
                rotatedBitmap = rotateImage(bitmap!!, 180F);

            ExifInterface.ORIENTATION_ROTATE_270 ->
                rotatedBitmap = rotateImage(bitmap!!, 270F);

            else -> rotatedBitmap = bitmap
        }
        return rotatedBitmap
    }


    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle);
        return Bitmap.createBitmap(
            source, 0, 0, source.getWidth(), source.getHeight(),
            matrix, true
        )
    }
}