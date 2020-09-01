package com.ale.balance_money.logic.transaction

import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.logic.account.Money
import com.ale.balance_money.logic.category.Category
import java.util.*
import kotlin.collections.ArrayList


/**
 * This class is for transactions, validate, logic
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
    fun fillListAccount(listAccount: List<AccountMoney>?): List<String> {
        val newListWithNameAccount: ArrayList<String> = ArrayList()
        if(listAccount != null) {
            for (item in listAccount) {
                newListWithNameAccount.add(item.title)
            }
        }
        return newListWithNameAccount
    }

    /**
     * this function fill all list of categories  with only name of category
     * @param listCategories
     * @return List<String>
     */
    fun fillListCategories(listCategories: List<Category>?): List<String> {
        val newListWithNameCategory: ArrayList<String> = ArrayList()
       if(listCategories != null){
           for (item in listCategories) {
               newListWithNameCategory.add(item.name)
           }
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
        val listWithSpeficifAccount = arrayListOf<AccountMoney>()
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
        val listError = arrayListOf<String>()
        when {
            transaction.amount == 0.0 -> {
                listError.add("El campo monto es requerido")
            }
            transaction.amount < 0 -> {
                listError.add("El monto debe ser mayor a 0")
            }
            transaction.amount > transaction.remainingAmount -> {
                listError.add("El monto de la transacción debe ser menor al saldo de la cuenta")
            }
            else -> {
                listError.add("")
            }
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
        val dateNow =   day.toString()+ "/" + (month + 1).toString() + "/" + year.toString()
        return dateNow
    }

    /**
     * This function get icon money for show with amount
     * @param money
     * @return String
     */
    fun getIconMonet(money: String):String {
        return when (money) {
            Money.COLON.name -> {
                "₡"
            }
            Money.DOLLAR.name -> {
                "$"
            }
            else -> {
                "€"
            }
        }
    }

    /**
     * this function check if size list of account is empty
     * @param size
     * @return Boolean
     */
    fun checkListAccountIsEmpty(size:Int):Boolean{
        return size == 0
    }
    /**
     * this function check if size list of account is empty
     * @param size
     * @return Boolean
     */
    fun checkListCategoriesIsEmpty(size:Int):Boolean{
        return size == 0
    }
}