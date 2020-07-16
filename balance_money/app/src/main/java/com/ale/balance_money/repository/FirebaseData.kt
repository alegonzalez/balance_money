package com.ale.balance_money.repository

import android.accounts.Account
import androidx.lifecycle.MutableLiveData
import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.logic.setting.DatabaseSetting
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.lang.Exception


class FirebaseData {

    private val databaseSetting = DatabaseSetting()
    private val ref = databaseSetting.getDatabaseReference()
    /**
     * This function get all account by user
     * get title,money,amount and description
     */
    fun getAccount():MutableLiveData<List<AccountMoney>>{
        val mutableData = MutableLiveData<List<AccountMoney>>()
        var listAccount = mutableListOf<AccountMoney>()
        val account= AccountMoney()
        var userId: String? =  databaseSetting.getUidUser()
            ref.child("account").child(userId.toString()).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listAccount.clear()
                    for (data in dataSnapshot.children) {
                        val accountData =
                            AccountMoney()
                        accountData.id = data.key.toString()
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

    /**
     * this function update detail of account
     */
    fun updateAccount(accountData: AccountMoney): Boolean {
        //return try {
            val uid = databaseSetting.getUidUser()
            ref.child("account").child(uid.toString()).child(accountData.id).setValue(accountData);
            return true
       // } catch (e: Exception) {
                //false
        //}
    }
    /**
     * This function create new account and save in firebase
     */
    fun createNewAccount(account: AccountMoney) {
        var uid = databaseSetting.getUidUser()
        val ref = databaseSetting.getDatabaseReference()
        ref.child("account").child(uid.toString()).push().setValue(account)
    }
    /**
     *this function remove a specific account from firebase
     * @return void
     */
    fun removeAccount(id:String): Boolean {
        return try {
            val uid = databaseSetting.getUidUser()
            val ref = databaseSetting.getDatabaseReference()
            ref.child("account").child(uid.toString()).child(id).removeValue();
            true
        } catch (e: Exception) {
            false
        }
    }
}