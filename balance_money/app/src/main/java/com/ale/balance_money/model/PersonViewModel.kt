package com.ale.balance_money.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ale.balance_money.logic.Person

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
        return Person().getAccountOfUser()
    }
}