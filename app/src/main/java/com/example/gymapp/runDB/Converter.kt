package com.example.gymapp.runDB

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converter {

    //converts a bitmap to a byteArray
    @TypeConverter
    fun bitToByte(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
        return outputStream.toByteArray()
    }

    //converts a byteArray to a bitmap
    @TypeConverter
    fun byteToBit(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}