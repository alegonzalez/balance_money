package com.ale.balance_money.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ale.balance_money.logic.Person
import com.ale.balance_money.repository.FirebaseUser

/**
 * view model person
 * @author Alejandro Alvarado González
 */
class PersonViewModel: ViewModel()  {
    var listAccountuser: List<Person>? = null

    /**
     * This function get account user list
     * @return LiveData<List<Person>>
     */
    fun getListAccountUser(): LiveData<List<Person>> {
        return FirebaseUser().getAccountsOfUser()
    }
    /**
     * This function get specific account user
     * @return LiveData<List<Person>>
     */
    fun getAccountUser(): LiveData<List<Person>> {
        return FirebaseUser().getAccountOfUser()
    }
}