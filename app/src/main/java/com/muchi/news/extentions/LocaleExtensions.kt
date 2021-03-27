/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

@file:Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION"
)

package com.muchi.news.extentions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
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
    if (this == null) {
        return false
    }
    if (this is Activity) {
        if (this.isDestroyed || this.isFinishing) {
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
    val dir = File(
        Environment.getExternalStorageDirectory(),
        "${this.resources.getString(R.string.app_name)}/Images"
    )

    if (!dir.exists()) {
        dir.mkdirs()
    }

    return dir
}

fun Context.documentsDirectory(): String? {
    val sdCardRoot = File(
        Environment.getExternalStorageDirectory(),
        "${this.resources.getString(R.string.app_name)}/Documents"
    )

    if (!sdCardRoot.exists()) {
        sdCardRoot.mkdirs()
    }

    return sdCardRoot.absolutePath
}
/* End Directory */

/* Start Base64 */
fun String.imageToBase64() : String {
    val bm = BitmapFactory.decodeFile(this)
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 25, baos)

    val b: ByteArray = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun Bitmap.bitmapToBase64() : String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 25, baos)

    val b: ByteArray = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun String.base64ToBitmap() : Bitmap {
    val decodedString: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}
/* End Base64 */

/* Start Date Time */
@SuppressLint("SimpleDateFormat")
fun String.dateSqlDDMMMYYYY() : String? {
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

@SuppressLint("SimpleDateFormat")
fun String.dateSqlDdMmmYy() : String? {
    var date: Date? = null
    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val returnFormatter = SimpleDateFormat("dd MMM yy")

    try {
        date = formatter.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return returnFormatter.format(date)
}

@SuppressLint("SimpleDateFormat")
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