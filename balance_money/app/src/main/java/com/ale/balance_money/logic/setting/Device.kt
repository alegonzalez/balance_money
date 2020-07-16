package com.ale.balance_money.logic.setting

import android.content.res.Configuration
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import com.google.android.material.snackbar.Snackbar
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

    /**
     * this function show message successful of any action
     */
    fun messageSuccessfulSnack(message: String, view: View?) {
        if(view!= null){
            val snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            snack.show()
        }

    }

    /**
     * this function show message when happen a mistake
     */
    fun messageMistakeSnack(message: String, view: View) {

        val snack =
            Snackbar.make(
                view,
                message,
                Snackbar.LENGTH_SHORT
            )
        val sandbarView: View = snack.view
        sandbarView.setBackgroundColor(Color.parseColor("#f44336"))
        snack.show()

    }
}