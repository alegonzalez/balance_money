package com.ale.balance_money.logic.statistics

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.EditText
import com.ale.balance_money.logic.transaction.Transaction
import org.joda.time.DateTimeComparator
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * validate date, and logic for stadistics
 * @author Alejandro alvarado
 */
class Stadistics(){


    /**
     * This function validate two field doesn't empty and start date is less than end date
     * @param startDate
     * @param endDate
     * @return Boolean
     */
    fun validateDates(startDate: EditText?, endDate: EditText?): Boolean {
        val dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        val dateStartSplit = startDate?.text.toString()
        val dateEndSplit = endDate?.text.toString()
        if (dateStartSplit == "" && dateEndSplit == "") {
            startDate?.error = "El campo fecha de inicio es requerido"
            endDate?.error = "El campo fecha final es requerido"
            return false
        } else if (dateStartSplit == "") {
            startDate?.error = "El campo fecha de inicio es requerido"
            return false
        } else if (dateEndSplit == "") {
            endDate?.error = "El campo fecha final es requerido"
            return false
        }else if( !this.validateDate(dateStartSplit) && !this.validateDate(dateEndSplit)){
            startDate?.error = "La fecha ingresada es incorrecta"
            endDate?.error = "La fecha ingresada es incorrecta"
            return false
        }else if( !this.validateDate(dateStartSplit)){
            startDate?.error = "La fecha ingresada es incorrecta"
            return false
        }else if( !this.validateDate(dateEndSplit)){
            endDate?.error = "La fecha ingresada es incorrecta"
            return false
        }else{
            val newStarDate = convertStringToDate(dateStartSplit.split("/")[2], dateStartSplit.split("/")[1], dateStartSplit.split("/")[0])
            val newEndDate = convertStringToDate(dateEndSplit.split("/")[2], dateEndSplit.split("/")[1], dateEndSplit.split("/")[0])
            val comparativeStartEndDate = dateTimeComparator.compare(newStarDate, newEndDate)
            val dateNow = Transaction().getDateNow().split("/")
            val currentDate = convertStringToDate(dateNow[2], dateNow[1], dateNow[0])
            val comparativeDateNowStart = dateTimeComparator.compare(currentDate, newStarDate)
            return when {
                comparativeStartEndDate > 0 -> {
                    startDate?.error = "La fecha de inicio tiene que ser menor o igual a la fecha final"
                    endDate?.error = "La fecha de final tiene que ser mayor o igual a la fecha inicial"
                    false
                }
                comparativeDateNowStart < 0 -> {
                    startDate?.error = "La fecha de inicio tiene que ser menor a la fecha actual"
                    false
                }
                else -> {
                    true
                }
            }
        }

    }

    /**
     * Validate format of date
     * @param date
     * @return Boolean
     */
    @Throws(ParseException::class)
    fun validateDate(dateStr: String):Boolean {
        val formatter = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        formatter.isLenient = false
        try {
            formatter.parse(dateStr)
        } catch (e: ParseException) {
            return false
        }
        return true
    }
    /**
     * This function convert date String to object date
     * @param year
     * @param mounth
     * @param day
     * @return Date
     */
    fun convertStringToDate(year: String, mounth: String, day: String): Date {
        return Date(year.toInt(), mounth.toInt() - 1, day.toInt())
    }

    /**
     * This function get all amount by type money
     * @param listTransaction
     * @param typeMoney
     * @return ArrayList<String>
     */
    fun getAllAmountbyMoney(
        listTransaction: List<Transaction>,
        typeMoney: String,
        startDate: Date,
        endDate: Date
    ): ArrayList<String> {
        val listIncomeExpense = arrayListOf<String>()
        val dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        var income: Double = 0.0
        var expense: Double = 0.0
        var temDate = Date()
        for (item in listTransaction) {
            temDate = convertStringToDate(
                item.dateOfTrasaction.split("/")[2],
                item.dateOfTrasaction.split("/")[1],
                item.dateOfTrasaction.split("/")[0]
            )
            val comparativeDateStart = dateTimeComparator.compare(startDate, temDate)
            val comparativeDateEnd = dateTimeComparator.compare(endDate, temDate)
            if (comparativeDateStart <= 0 && comparativeDateEnd >= 0) {
                if (typeMoney == item.money && item.typeTransaction == "Ingreso") {
                    income += item.amount
                } else if (typeMoney == item.money && item.typeTransaction == "Gasto") {
                    expense += item.amount
                }
            }
        }
        listIncomeExpense.add(income.toString())
        listIncomeExpense.add(expense.toString())
        return listIncomeExpense
    }

    /**
     * This function convert date to set in field
     * @param it
     * @return Date
     */
    fun getconvertDate(it:Long):String{
        val simpleFormat = SimpleDateFormat("d/M/yyyy", Locale.US)
        var date = Date(it)
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, 1)
        date = c.time
        return simpleFormat.format(date)
    }

}