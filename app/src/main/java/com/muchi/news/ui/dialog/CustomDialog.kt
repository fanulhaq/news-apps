/*
 * Copyright (c) 2021 - Muchi (Irfanul Haq).
 */

@file:SuppressLint("InflateParams")
@file:Suppress("DEPRECATION")

package com.muchi.news.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.muchi.news.R


fun Context.bottomSheetNoInternet(layoutInflater: LayoutInflater): BottomSheetDialog {
    val view = layoutInflater.inflate(R.layout.bs_no_internet, null)
    val bsDialog = BottomSheetDialog(this)
    bsDialog.setContentView(view)

    val imageBack: ImageView = view.findViewById(R.id.imageBack)
    imageBack.setOnClickListener {
        bsDialog.dismiss()
    }

    return bsDialog
}


fun Context.bottomSheetError(layoutInflater: LayoutInflater, msg: String){
    val view = layoutInflater.inflate(R.layout.bs_error, null)
    val bsDialog = BottomSheetDialog(this)
    bsDialog.setContentView(view)

    val imageBack: ImageView = view.findViewById(R.id.imageBack)
    val tvMsg: TextView = view.findViewById(R.id.tvMsg)
    tvMsg.text = msg

    imageBack.setOnClickListener {
        bsDialog.dismiss()
    }

    bsDialog.show()
}