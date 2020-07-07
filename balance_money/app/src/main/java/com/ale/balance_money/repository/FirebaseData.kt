package com.ale.balance_money.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ale.balance_money.logic.Account
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FirebaseData {
    private val ref = FirebaseDatabase.getInstance().reference

    /**
     * This function get all account by user
     * get title,money,amount and description
     */
    fun getAccount():LiveData<MutableList<Account>>{
        val mutableData = MutableLiveData<MutableList<Account>>()
        var listAccount = mutableListOf<Account>()
        val account= Account()
        var userId: String? =  account.getUidUser()
        ref.child("account").child(userId.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val accountData = Account()
                    accountData.title  = data.child("title").value.toString()
                    accountData.money = data.child("money").value.toString()
                    accountData.amount = data.child("amount").value.toString().toDouble()
                    accountData.description = data.child("description").value.toString()
                   listAccount.add(accountData)
                }
                mutableData.value = listAccount
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Log.w(TAG, "getUser:onCancelled", databaseError.toException())
                // ...
            }
        })

        return mutableData
    }
}