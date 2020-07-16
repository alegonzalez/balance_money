package com.ale.balance_money.logic.account

import java.util.ArrayList

enum class Money {
    COLON, DOLLAR, EURO
}

/**
 * This class is all logic of account
 * @author Alejandro Alvarado
 */
class AccountMoney {
    var id: String = ""
    var title: String = ""
    var money: String = ""
    var amount: Double = 0.0
    var description: String = ""

    /**
     * This function validate that field aren't empty
     * @param title
     * @param typeMoney
     * @param amount
     * @return ArrayList<String>
     */
    fun validateFieldsAccount(
        title: String?,
        typeMoney: String,
        amount: String?
    ): ArrayList<String> {
        val listError: ArrayList<String> = ArrayList()
        if (title == "") {
            listError.add("El campo nombre es requerido")
        } else {
            listError.add("")
        }
        if (typeMoney == "") {
            listError.add("Debes seleccionar el tipo de moneda")
        } else {
            listError.add("")
        }
        if (amount == "") {
            listError.add("El campo monto es requerido")
        } else {
            listError.add("")
        }

        return listError
    }
}