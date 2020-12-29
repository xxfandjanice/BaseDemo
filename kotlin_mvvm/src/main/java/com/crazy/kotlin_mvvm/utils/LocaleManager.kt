package com.crazy.kotlin_mvvm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.preference.PreferenceManager
import java.util.*

/**
 * 修改语音
 */
object LocaleManager {

    private val LANGUAGE_KEY = "language_key"
    val LANGUAGE_UPDATE = "language_update"
    val LANGUAGE_ENGLISH = "en"
    val LANGUAGE_JAPAN = "ja"
    val LANGUAGE_KOREAN = "ko"


    fun setLocale(c: Context): Context {
        return updateResources(c, getLanguage(c))
    }

    //根据手机版本号进行设置语言环境
    fun setNewLocale(c: Context, language: String): Context {
        persistLanguage(c, language)
        return updateResources(c, language)
    }

    //SharedPreferences获取语言环境
    fun getLanguage(c: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(c)
        return prefs.getString(
            LANGUAGE_KEY,
            LANGUAGE_ENGLISH
        )
    }

    @SuppressLint("ApplySharedPref")
     fun persistLanguage(c: Context, language: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(c)
        // use commit() instead of apply(), because sometimes we kill the application process immediately
        // which will prevent apply() to finish
        prefs.edit().putString(LANGUAGE_KEY, language).commit()
    }

    fun changeAppLanguage(context: Context) {
        val resources = context.resources
        val configuration = resources.configuration
        // app locale
        val locale = when (getLanguage(context)) {
            LANGUAGE_ENGLISH -> Locale.ENGLISH
            LANGUAGE_JAPAN -> Locale.JAPANESE
            LANGUAGE_KOREAN -> Locale.KOREAN
            else -> Locale.ENGLISH
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
        } else {
            configuration.locale = locale
        }
        // updateConfiguration
        val dm = resources.displayMetrics
        resources.updateConfiguration(configuration, dm)
    }

    private fun updateResources(context: Context, language: String?): Context {
        val locale = when (language) {
            LANGUAGE_ENGLISH -> Locale.ENGLISH
            LANGUAGE_JAPAN -> Locale.JAPANESE
            LANGUAGE_KOREAN -> Locale.KOREAN
            else -> Locale.ENGLISH
        }

        val res = context.resources
        val config = res.configuration
        //做版本兼容性判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        } else {
            config.setLocale(locale)
            res.updateConfiguration(config, res.displayMetrics)
        }
        return context
    }

    fun getLocale(res: Resources): Locale {
        val config = res.configuration
        return if (Build.VERSION.SDK_INT >= 24) config.locales.get(0) else config.locale
    }
}