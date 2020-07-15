package com.ale.balance_money.logic

import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.WindowManager
import kotlin.math.sqrt

/**
 * this class is for any general configuration or information that you want of device
 * @author Alejandro Alvarado
 */
class Device {

    /**
     * This function get orientation of device
     * @param orientation
     * @return boolean
     */
    fun detectOrientationDevice(orientation: Int?):Boolean{
          return orientation != Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * This function detect if device is tablet or smartphone
     * true is smartphone and false is tablet
     * @param windowManager
     * @return Boolean
     */
     fun detectTypeDevice(windowManager:WindowManager): Boolean {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val yInches = metrics.heightPixels / metrics.ydpi
        val xInches = metrics.widthPixels / metrics.xdpi
        val diagonalInches = sqrt(xInches * xInches + yInches * yInches.toDouble())
        return diagonalInches < 6.5
    }
}