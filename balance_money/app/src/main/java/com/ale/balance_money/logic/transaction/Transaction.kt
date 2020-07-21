package com.ale.balance_money.logic.transaction

import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.logic.category.Category
import java.util.*
import kotlin.collections.ArrayList


/**
 * This class is for transactions, validate ...
 * @author Alejandro Alvarado
 */
class Transaction {
    var id: String = ""
    var account: String = ""
    var category: String = ""
    var amount: Double = 0.0
    var money: String = ""
    var typeTransaction = ""
    var description: String = ""
    var dateOfTrasaction: String = ""
    var remainingAmount: Double = 0.0

    /**
     * this function fill all list of account  with only name of account
     * @param listAccount
     * @return List<String>
     */
    fun fillListAccount(listAccount: List<AccountMoney>): List<String> {
        val newListWithNameAccount: ArrayList<String> = ArrayList()
        for (item in listAccount) {
            newListWithNameAccount.add(item.title)
        }
        return newListWithNameAccount
    }

    /**
     * this function fill all list of categories  with only name of category
     * @param listCategories
     * @return List<String>
     */
    fun fillListCategories(listCategories: List<Category>): List<String> {
        val newListWithNameCategory: ArrayList<String> = ArrayList()
        for (item in listCategories) {
            newListWithNameCategory.add(item.name)
        }
        return newListWithNameCategory
    }

    /**
     * This function get amount of a specific account
     * @param listAccount
     * @param nameAccount
     * @return Double
     */
    fun getAmountByAccountSelected(
        listAccount: List<AccountMoney>?,
        nameAccount: String
    ): ArrayList<AccountMoney> {
        var listWithSpeficifAccount = arrayListOf<AccountMoney>()
        if (listAccount != null) {
            for (item in listAccount) {
                if (item.title == nameAccount) {
                    listWithSpeficifAccount.add(item)
                }
            }
        }
        return listWithSpeficifAccount
    }

    /**
     * This function get all error of transaction
     * @param transaction
     * @return ArrayList<String>
     */
    fun validateFieldToMakeTransaction(transaction: Transaction): ArrayList<String> {
        var listError = arrayListOf<String>()
        if (transaction.amount == 0.0) {
            listError.add("El campo monto es requerido")
        } else if (transaction.amount < 0) {
            listError.add("El monto debe ser mayor a 0")
        } else if (transaction.amount > transaction.remainingAmount) {
            listError.add("El monto de la transacción debe ser menor al saldo de la cuenta")
        } else {
            listError.add("")
        }
        if (transaction.typeTransaction == "") {
            listError.add("Debes de elegir el tipo de transacción, ingreso o gasto")
        } else {
            listError.add("")
        }
        return listError
    }

    /**
     * This function get date when user make transaction
     * @return String
     */
    fun getDateNow(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dateNow = year.toString() + "/" + (month + 1).toString() + "/" + day.toString()
        return dateNow
    }
}