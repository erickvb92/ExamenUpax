package com.upax.aplicationupax.db

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.upax.aplicationupax.BuildConfig


private const val DELIMITER = "|"

class SharedPreferencesManager (application: Application) : Storage {

    private val sharedPreferences = application.getSharedPreferences(
        BuildConfig.APPLICATION_ID,
        Context.MODE_PRIVATE
    )
    private val editor = sharedPreferences.edit()
    private val prefixKey = BuildConfig.APPLICATION_ID + "."

    override fun saveString(key: String, value: String, async: Boolean) {
        editor.putString(prefixKey + key, value).let {
            if (async) it.apply() else it.commit()
        }
    }

    override fun saveListString(key: String, value: List<String>, async: Boolean) {
        var stringTemp = ""
        value.forEach { text ->
            stringTemp += "$text$DELIMITER"
        }
        saveString(key, stringTemp.substringBeforeLast(DELIMITER), async)
    }

    override fun saveInt(key: String, value: Int, async: Boolean) {
        editor.putInt(prefixKey + key, value).let {
            if (async) it.apply() else it.commit()
        }
    }

    override fun saveFloat(key: String, value: Float, async: Boolean) {
        editor.putFloat(prefixKey + key, value).let {
            if (async) it.apply() else it.commit()
        }
    }

    override fun saveLong(key: String, value: Long, async: Boolean) {
        editor.putLong(prefixKey + key, value).let {
            if (async) it.apply() else it.commit()
        }
    }

    override fun saveBoolean(key: String, value: Boolean, async: Boolean) {
        editor.putBoolean(prefixKey + key, value).let {
            if (async) it.apply() else it.commit()
        }
    }

    override fun saveStringSet(key: String, value: Set<String>, async: Boolean) {
        editor.putStringSet(prefixKey + key, value).let {
            if (async) it.apply() else it.commit()
        }
    }

    override fun validate(key: String) =
        sharedPreferences.contains(prefixKey + key)


    @SuppressLint("ApplySharedPref")
    override fun removeAll() {
        sharedPreferences.edit().clear().commit()
    }

    override fun delete(key: String) =
        editor.remove(prefixKey + key).apply()

    override fun getString(key: String) = sharedPreferences.getString(prefixKey + key, null)

    override fun getListString(key: String): List<String> {
        val string = sharedPreferences.getString(prefixKey + key, null)
        return if (string.isNullOrEmpty()) emptyList() else string.split(DELIMITER)
    }

    override fun getStringSet(key: String) =
        sharedPreferences.getStringSet(prefixKey + key, HashSet<String>())

    override fun getInt(key: String) = sharedPreferences.getInt(prefixKey + key, -1)

    override fun getFloat(key: String) = sharedPreferences.getFloat(prefixKey + key, -1f)

    override fun getLong(key: String) = sharedPreferences.getLong(prefixKey + key, -1)

    override fun getBoolean(key: String) = sharedPreferences.getBoolean(prefixKey + key, false)

}