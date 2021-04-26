/*
 * Copyright (c) 2021. ~ Irfanul Haq.
 */

package dependencies

object Build {
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val butterknife_gradle_plugin = "com.jakewharton:butterknife-gradle-plugin:${Versions.butterknife}"
    const val dagger_hilt_plugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.dagger_hilt_plugin}"
    const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktlint}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlytics}"
    const val google_services = "com.google.gms:google-services:${Versions.google_services}"
}