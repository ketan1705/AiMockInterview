package com.ken.aimockinterview.utils

import android.content.Context
import androidx.core.content.edit

class OnBoardingUtils(private val context: Context) {
    fun isOnBoardingDone(): Boolean {
        val sharedPref = context.getSharedPreferences(
            Constants.ON_BOARDING,
            Context.MODE_PRIVATE
        )
        return sharedPref.getBoolean("done", false)
    }

    fun setOnBoardingDone() {
        val sharedPref = context.getSharedPreferences(
            Constants.ON_BOARDING,
            Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putBoolean("done", true)
        editor.apply()
    }

    fun userIsLoggedIn(): Boolean {
        val sharedPref = context.getSharedPreferences(
            Constants.USER_LOGIN,
            Context.MODE_PRIVATE
        )
        return sharedPref.getBoolean(Constants.LOGIN, false)
    }

    fun setUserLoggedIn() {
        val sharedPref = context.getSharedPreferences(
            Constants.USER_LOGIN,
            Context.MODE_PRIVATE
        )
        sharedPref.edit() {
            putBoolean(Constants.LOGIN, true)
        }
    }

    fun clearUserLoggedIn() {
        val sharedPref = context.getSharedPreferences(
            Constants.USER_LOGIN,
            Context.MODE_PRIVATE
        )
        sharedPref.edit() {
            clear()
        }
    }
}