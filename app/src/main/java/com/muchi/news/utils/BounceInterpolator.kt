/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.utils

import android.view.animation.Interpolator
import kotlin.math.cos
import kotlin.math.pow

class BounceInterpolator(amplitude: Double, frequency: Double) : Interpolator {

    private var mAmplitude = amplitude
    private var mFrequency = frequency

    override fun getInterpolation(time: Float): Float {
        return ((-1 * Math.E.pow(-time / mAmplitude) * cos(mFrequency * time) + 1)).toFloat()
    }
}