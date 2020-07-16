package com.ale.balance_money.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.repository.FirebaseData


class AccountViewModel():ViewModel() {


     var listAccount: List<AccountMoney>? = null

    /**
     * This function get all account of user by id of user from firebase
     */
    fun fetchAccount():LiveData<List<AccountMoney>>{
        val firebaseData = FirebaseData()
        return firebaseData.getAccount()
    }
 }

