package com.ale.balance_money.logic.setting

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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
        return FirebaseAuth.getInstance().currentUser?.uid
    }
    /**
     *this function get database reference of firebase
     * return DatabaseReference
     */
    fun getDatabaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }
}