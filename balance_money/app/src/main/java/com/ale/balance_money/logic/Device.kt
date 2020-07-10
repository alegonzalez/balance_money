package com.ale.balance_money.logic

import android.content.res.Configuration
import android.hardware.display.DisplayManager
import android.util.DisplayMetrics
import android.view.Display.DEFAULT_DISPLAY
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import kotlin.math.sqrt


class Device {

    /**
     * This function get orientation of device
     * @return boolean
     */
    fun detectOrientationDevice(orientation: Int?):Boolean{
          return orientation != Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * This function detect if device is tablet or smartphone
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