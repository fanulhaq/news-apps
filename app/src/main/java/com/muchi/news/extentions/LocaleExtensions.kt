/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

@file:Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION"
)

@file:SuppressLint("SimpleDateFormat")

package com.muchi.news.extentions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.muchi.news.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.*
import java.util.*


fun Context?.toastShort(string: String){
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

fun Context?.toastLong(string: String){
    Toast.makeText(this, string, Toast.LENGTH_LONG).show()
}

fun Context?.isNetworkAvailable(): Boolean {
    val connectivityManager = this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager?.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Context?.isValidContextForGlide(): Boolean {
    if(this == null) {
        return false
    }

    if(this is Activity) {
        if(this.isDestroyed || this.isFinishing) {
            return false
        }
    }

    return true
}

fun String.toCurrency() : String? {
    return NumberFormat.getNumberInstance(Locale.getDefault()).format(this.toLong())
}

fun logTest(string: String){
    Log.d("TEST", string)
}

fun showLog(tag: String, string: String){
    Log.d(tag, string)
}

/* Start Directory */
fun Context.imageDirectory(): File {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
        File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}" +
                "${File.separator}${this.resources.getString(R.string.app_name)}${File.separator}Images")
    } else {
        File(Environment.getExternalStorageDirectory().absolutePath +
                "${File.separator}${this.resources.getString(R.string.app_name)}${File.separator}Images")
    }
}

fun Context.documentsDirectory(): String? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
        "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}" +
                "${File.separator}${this.resources.getString(R.string.app_name)}${File.separator}Documents"
    } else { Environment.getExternalStorageDirectory().absolutePath +
                "${File.separator}${this.resources.getString(R.string.app_name)}${File.separator}Documents"
    }
}
/* End Directory */

/* Start Base64 */
fun String.imageToBase64() : String {
    return try {
        val bm = BitmapFactory.decodeFile(this)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos)

        val b: ByteArray = baos.toByteArray()
        Base64.encodeToString(b, Base64.DEFAULT)
    } catch (e: NullPointerException){
        e.printStackTrace()
        ""
    } catch (e: RuntimeException){
        e.printStackTrace()
        ""
    }
}

fun Bitmap.bitmapToBase64() : String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 20, baos)

    val b: ByteArray = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun String.base64ToBitmap() : Bitmap {
    val decodedString: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}
/* End Base64 */

/* Start Date Time */
fun String.dateSqlToDdMmmYyyy() : String? {
    var date: Date? = null
    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val returnFormatter = SimpleDateFormat("dd MMM yyyy")

    try {
        date = formatter.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return returnFormatter.format(date)
}

fun String.dateSqlToFullDate() : String? {
    var date: Date? = null
    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val returnFormatter = SimpleDateFormat("dd MMMM yyyy")

    try {
        date = formatter.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return returnFormatter.format(date)
}

fun String.timesNewsToDateTime() : String? {
    val data = this.replace("T", " ").replace("Z", "")

    var date: Date? = null
    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    val returnFormatter = SimpleDateFormat("dd MMM yyyy - hh:mm")

    try {
        date = formatter.parse(data)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return returnFormatter.format(date)
}

fun getLocaleCurrentSqlDate() : String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
}

fun getLocaleCurrentTime() : String {
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
}

fun getLocaleCurrentDateTime() : String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
}
/* End Date Time */