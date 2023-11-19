package com.jedisebas.tconnect

import android.graphics.Color

class MyColors {
    companion object {
        fun brightColor(color: Int, value: Float): Int {
            val red = (Color.red(color) * (1 - value) + 255 * value).toInt()
            val green = (Color.green(color) * (1 - value) + 255 * value).toInt()
            val blue = (Color.blue(color) * (1 - value) + 255 * value).toInt()
            return Color.rgb(red, green, blue)
        }

        fun darkColor(color: Int, value: Float): Int {
            val red = (Color.red(color) * (1 - value)).toInt()
            val green = (Color.green(color) * (1 - value)).toInt()
            val blue = (Color.blue(color) * (1 - value)).toInt()
            return Color.rgb(red, green, blue)
        }
    }
}