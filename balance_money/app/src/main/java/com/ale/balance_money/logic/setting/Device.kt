package com.ale.balance_money.logic.setting

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.ale.balance_money.R
import com.ale.balance_money.logic.Person
import com.facebook.FacebookSdk.getApplicationContext
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
    fun detectOrientationDevice(orientation: Int?): Boolean {
        return orientation != Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * This function detect if device is tablet or smartphone
     * true is smartphone and false is tablet
     * @param windowManager
     * @return Boolean
     */
    fun detectTypeDevice(windowManager: WindowManager): Boolean {
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
        if (view != null) {
            val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            val custom = checkSnackBar(view)
            val iconSnackBar = custom.findViewById<ImageView>(R.id.idImageSnackbar)
            iconSnackBar.setImageResource(R.drawable.ic_baseline_info_24)
            val txtTextSnackBar = custom.findViewById<TextView>(R.id.txtTextSnackBar)
            txtTextSnackBar.setText(message)
            snack.view.setBackgroundColor(Color.TRANSPARENT)
            val snackbarLayout: Snackbar.SnackbarLayout = snack.view as Snackbar.SnackbarLayout
            snackbarLayout.setPadding(0, 0, 0, 0)
            snackbarLayout.addView(custom)
            snack.show()
        }
    }

    /**
     * This function check layout snackbar to use
     * @param view
     * @return View
     */
    fun checkSnackBar(view:View):View{
        val inflater = LayoutInflater.from(view.context);
        return if (detectTypeDevice(view.context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager)) {
            inflater.inflate(R.layout.snackbar_custom, null)
        }else{
            inflater.inflate(R.layout.snackbar_custom_tablet, null)
        }
    }
    /**
     * this function show message when happen a mistake
     */
    fun messageMistakeSnack(message: String, view: View) {

        val snack = Snackbar.make(view,message,Snackbar.LENGTH_LONG)
         val custom = checkSnackBar(view)
        val iconSnackBar = custom.findViewById<ImageView>(R.id.idImageSnackbar)
        iconSnackBar.setImageResource(R.drawable.ic_baseline_warning_24)
        val txtTextSnackBar = custom.findViewById<TextView>(R.id.txtTextSnackBar)
        val cardView = custom.findViewById<CardView>(R.id.cardViewSnackBar)
        cardView.setCardBackgroundColor(Color.parseColor("#f44336"))
        txtTextSnackBar.text = message
        snack.view.setBackgroundColor(Color.TRANSPARENT)
        val snackbarLayout: Snackbar.SnackbarLayout = snack.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(custom)
        snack.show()
    }

    /**
     *this function check if device has internet
     * @param context
     * @return Boolean
     */
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }
    /**
     * This function get provider of user authenticated
     * @return String
     */
    fun getProviderUser(): String? {
        val context = getApplicationContext()
        val prefs = context.getSharedPreferences(
            context.resources.getString(R.string.pref_file),
            Context.MODE_PRIVATE
        )
        return prefs.getString("provider", null)
    }

    /**
     * This function get password of user authenticated
     * @return String
     */
    fun getUser(): Person {
        val context = getApplicationContext()
        val prefs = context.getSharedPreferences(
            context.getResources().getString(R.string.pref_file),
            Context.MODE_PRIVATE
        )
        val user = Person()
        user.email = prefs.getString("email", null).toString()
        user.name = prefs.getString("name", null).toString()
        user.password = prefs.getString("password", null).toString()
        user.provider = prefs.getString("provider", null).toString()
        return user
    }

}