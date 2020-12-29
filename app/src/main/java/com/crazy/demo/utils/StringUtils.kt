package com.crazy.demo.utils

import android.text.TextUtils

class StringUtils {
    companion object{
        fun isEmail(string: String):Boolean{
            if (TextUtils.isEmpty(string)) return false

            return string.contains("@")
        }
    }
}