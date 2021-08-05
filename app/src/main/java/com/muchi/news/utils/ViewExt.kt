/*
 * Copyright (c) 2021. ~ Irfanul Haq.
 */

@file:Suppress("DEPRECATION")

package com.muchi.news.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils.*
import android.view.inputmethod.InputMethodManager
import androidx.core.graphics.drawable.DrawableCompat
import com.muchi.news.R

fun View.didTapButton() {
    val myAnim: Animation = loadAnimation(context, R.anim.bounce)
    val interpolator = BounceInterpolator(0.12, 12.0)
    myAnim.interpolator = interpolator
    this.startAnimation(myAnim)
}

fun View.animSlideUp() {
    val anim = loadAnimation(context, R.anim.slide_up)
    this.startAnimation(anim)
}

fun View.animSlideDown() {
    val anim = loadAnimation(context, R.anim.slide_down)
    this.startAnimation(anim)
}

fun View.animFadeIn() {
    val anim = loadAnimation(context, R.anim.fade_in)
    this.startAnimation(anim)
}

fun View.animFadeOut() {
    val anim = loadAnimation(context, R.anim.fade_out)
    this.startAnimation(anim)
}

fun View.visible() {
    this.visibility = VISIBLE
}

fun View.invisible() {
    this.visibility = INVISIBLE
}

fun View.gone() {
    this.visibility = GONE
}

fun View.changeBackgroundDrawableColor(color: String?) {
    val backgroundDrawable = DrawableCompat.wrap(this.background).mutate()
    DrawableCompat.setTint(backgroundDrawable, Color.parseColor("$color"))
}

fun View.createDrawableFromView(context: Context): Bitmap {
    val displayMetrics = DisplayMetrics()
    (context as Activity).windowManager.defaultDisplay
        .getMetrics(displayMetrics)
    this.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)

    this.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
    this.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)

    this.buildDrawingCache()
    val bitmap = Bitmap.createBitmap(
        this.measuredWidth, this.measuredHeight, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)
    this.draw(canvas)
    return bitmap
}

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if(Build.VERSION.SDK_INT >= 26) {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.post {
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            imm.hideSoftInputFromInputMethod(currentFocus?.windowToken, 0)
        }
    } else {
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            imm.hideSoftInputFromInputMethod(view.windowToken, 0)
        }
    }
}

fun Activity.showKeyboard(){
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}