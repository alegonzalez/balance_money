package com.ale.balance_money.repository

import androidx.lifecycle.MutableLiveData
import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.logic.setting.DatabaseSetting
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

/**
 * This class is for any communication with firebase for example an insert, update or delete for account
 * @author Alejandro Alvarado
 */
class FirebaseData {

    private val databaseSetting = DatabaseSetting()
    private val ref = databaseSetting.getDatabaseReference()
    /**
     * This function get all account by user
     * get title,money,amount and description
     * @return MutableLiveData<List<AccountMoney>>
     */
    fun getAccount():MutableLiveData<List<AccountMoney>>{
        val mutableData = MutableLiveData<List<AccountMoney>>()
        var listAccount = mutableListOf<AccountMoney>()
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
     * @param accountData
     * @return Boolean
     */
    fun updateAccount(accountData: AccountMoney): Boolean {
         try {
            val uid = databaseSetting.getUidUser()
            ref.child("account").child(uid.toString()).child(accountData.id).setValue(accountData);

        } catch (e: Exception) {
                return false
        }
        return true
    }
    /**
     * This function create new account and save in firebase
     * @param account
     * return void
     */
    fun createNewAccount(account: AccountMoney) {
        val uid = databaseSetting.getUidUser()
        val ref = databaseSetting.getDatabaseReference()
        ref.child("account").child(uid.toString()).push().setValue(account)
    }
    /**
     *this function remove a specific account from firebase
     * @param id
     * @return Boolean
     */
    fun removeAccount(id:String): Boolean {
         try {
            val uid = databaseSetting.getUidUser()
            val ref = databaseSetting.getDatabaseReference()
            ref.child("account").child(uid.toString()).child(id).removeValue()
        } catch (e: Exception) {
           return false
        }
        return true
    }
}