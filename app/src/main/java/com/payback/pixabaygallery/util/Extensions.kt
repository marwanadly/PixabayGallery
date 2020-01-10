package com.payback.pixabaygallery.util

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }