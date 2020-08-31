package com.ale.balance_money.logic.setting

import android.content.Context
import com.ale.balance_money.R
import com.facebook.FacebookSdk.getApplicationContext
import com.google.firebase.database.*

/**
 * this class show all method for get reference to database
 * @author Alejandro Alvarado
 */
class DatabaseSetting {

    /**
     * This function get uid of user authenticated
     * @return String
     */
    fun getUidUser(): String? {
        val context = getApplicationContext();
        val prefs = context.getSharedPreferences(context.getResources().getString(R.string.pref_file), Context.MODE_PRIVATE)
        return prefs.getString("uidUser", null)
    }
    /**
     *this function get database reference of firebase
     * return DatabaseReference
     */
    fun getDatabaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }


}