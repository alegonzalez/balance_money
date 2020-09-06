package com.ale.balance_money.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ale.balance_money.logic.setting.DatabaseSetting
import com.ale.balance_money.logic.transaction.Transaction
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class FirebaseTransaction {
    private val uidUser = DatabaseSetting().getUidUser()
    private val ref = DatabaseSetting().getDatabaseReference()

    /**
     * This function get all transactions by user
     * @return LiveData<List<Transactions>>
     */
    fun getAllTransactions(): LiveData<List<Transaction>> {
        val mutableData = MutableLiveData<List<Transaction>>()
        val listTransaction = mutableListOf<Transaction>()
        ref.child("transaction").child(uidUser.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listTransaction.clear()
                for (data in dataSnapshot.children) {
                    val transaction = Transaction()
                    transaction.id = data.key.toString()
                    transaction.account = data.child("account").value.toString()
                    transaction.amount = data.child("amount").value.toString().toDouble()
                    transaction.category = data.child("category").value.toString()
                    transaction.dateOfTrasaction = data.child("dateOfTrasaction").value.toString()
                    transaction.description = data.child("description").value.toString()
                    transaction.money = data.child("money").value.toString()
                    transaction.typeTransaction = data.child("typeTransaction").value.toString()
                    listTransaction.add(transaction)
                }
                mutableData.value = listTransaction
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Log.w(TAG, "getUser:onCancelled", databaseError.toException())
                // ...
            }

        })
        return mutableData
    }

    /**
     * this function insert new transaction in firebase
     * @param transaction
     * @return Boolean
     */
    fun insertNewTransaction(transaction: Transaction): Boolean {
        try {
            ref.child("transaction").child(uidUser.toString()).push().setValue(transaction)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    /**
     *this function remove a specific transaction from firebase
     * @param id
     * @return Boolean
     */
    fun removeTransaction(id:String): Boolean {
        try {
            ref.child("transaction").child(uidUser.toString()).child(id).removeValue()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    /**
     * This function get account selected by user for select transaction of the account
     * @param nameAccount
     * @return mutableData LiveData<List<Transaction>>
     */
    fun getTrasactionByAccount(nameAccount: String): LiveData<List<Transaction>> {
        val mutableData = MutableLiveData<List<Transaction>>()
        val listTransaction = mutableListOf<Transaction>()
        ref.child("transaction").child(uidUser.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listTransaction.clear()
                for (data in dataSnapshot.children) {
                    val transaction = Transaction()
                    transaction.account = data.child("account").value.toString()
                    if(transaction.account == nameAccount){
                        transaction.id = data.key.toString()
                        transaction.amount = data.child("amount").value.toString().toDouble()
                        transaction.category = data.child("category").value.toString()
                        transaction.dateOfTrasaction = data.child("dateOfTrasaction").value.toString()
                        transaction.description = data.child("description").value.toString()
                        transaction.money = data.child("money").value.toString()
                        transaction.typeTransaction = data.child("typeTransaction").value.toString()
                        listTransaction.add(transaction)
                    }
                }
                mutableData.value = listTransaction
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Log.w(TAG, "getUser:onCancelled", databaseError.toException())
                // ...
            }

        })
        return mutableData
    }
}