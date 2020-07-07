package com.ale.balance_money.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ale.balance_money.logic.Account
import com.ale.balance_money.repository.FirebaseData

class AccountViewModel:ViewModel() {
    val firebaseData = FirebaseData()

    /**
     * This function get all account of user by id of user from firebase
     */
    fun fetchAccount():LiveData<MutableList<Account>>{
        val mutableData = MutableLiveData<MutableList<Account>>()
        firebaseData.getAccount().observeForever{accountList->
            mutableData.value = accountList
        }
        return mutableData
    }
}