package com.example.powercrew.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleHelper {

    fun setAppLocale(context: Context): Context {
        val locale = Locale.getDefault()
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        saveLanguage(context, locale.language)
        return context.createConfigurationContext(config) // استخدام الطريقة الحديثة
    }

    fun setLocale(context: Context, languageCode: String) {

        updateResources(context, languageCode)
    }

    fun loadLocale(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val savedLang = sharedPreferences.getString("App_Lang", null)

        val systemLang = Locale.getDefault().language
        val languageToApply = savedLang ?: systemLang

        updateResources(context, languageToApply)
    }

    private fun saveLanguage(context: Context, languageCode: String) {
        val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("App_Lang", languageCode).apply()
    }

    private fun updateResources(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = Configuration(resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(android.app.LocaleManager::class.java)
            localeManager?.applicationLocales = android.os.LocaleList.forLanguageTags(languageCode)
        } else {
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }

}
