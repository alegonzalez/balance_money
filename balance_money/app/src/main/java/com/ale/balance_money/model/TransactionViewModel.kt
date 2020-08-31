package com.ale.balance_money.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ale.balance_money.logic.transaction.Transaction
import com.ale.balance_money.repository.FirebaseTransaction

class TransactionViewModel:ViewModel() {
  var listTransaction: List<Transaction>? = null

  fun getListTransaction(): LiveData<List<Transaction>> {
    return FirebaseTransaction().getAllTransactions()
  }


}