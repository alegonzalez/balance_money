package com.ale.balance_money.logic.account

import android.widget.Button
import com.ale.balance_money.R
import com.facebook.FacebookSdk
import java.io.Serializable
import java.util.*


enum class Money {
    COLON, DOLLAR, EURO
}

/**
 * This class is all logic of account
 * @author Alejandro Alvarado
 */
class AccountMoney:Serializable {
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
    /**
     * This function set visibility money that was selected by user
     * @param money
     * @param indicatorColon
     * @param indicatorDollar
     * @param indicatorEuro
     */
     fun setMoney(
        money: String?,
        orientation:Boolean,
        btnColon: Button,
        btnDollar: Button,
        btnEuro: Button
    ): String {
        val context = FacebookSdk.getApplicationContext();
        if(orientation){
            //Smartphone
            when (money) {
                Money.COLON.name -> {
                    btnColon.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.colon), null, context.resources.getDrawable(
                        R.drawable.check), null);
                    btnEuro.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.euro), null, null, null)
                    btnDollar.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.dollar), null, null, null)
                    return Money.COLON.name
                }
                Money.DOLLAR.name -> {
                    btnDollar.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.dollar),null,context.resources.getDrawable(
                        R.drawable.check),null);
                    btnEuro.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.euro), null, null, null)
                    btnColon.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.colon), null, null, null)
                    return Money.DOLLAR.name
                }
                Money.EURO.name -> {
                    btnEuro.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.euro), null, context.resources.getDrawable(
                        R.drawable.check), null)
                    btnDollar.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.dollar), null, null, null)
                    btnColon.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.colon), null, null, null)
                    return Money.EURO.name
                }
            }
        }else{
            //Tablet
            when (money) {
                Money.COLON.name -> {
                    btnColon.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.colon_tablet), null, context.resources.getDrawable(
                        R.drawable.check_tablet), null);
                    btnEuro.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.euro_tablet), null, null, null)
                    btnDollar.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.dollar_tablet), null, null, null)
                    return Money.COLON.name
                }
                Money.DOLLAR.name -> {
                    btnDollar.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.dollar_tablet),null,context.resources.getDrawable(
                        R.drawable.check_tablet),null);
                    btnEuro.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.euro_tablet), null, null, null)
                    btnColon.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.colon_tablet), null, null, null)
                    return Money.DOLLAR.name
                }
                Money.EURO.name -> {
                    btnEuro.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.euro_tablet), null, context.resources.getDrawable(
                        R.drawable.check_tablet), null)
                    btnDollar.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.dollar_tablet), null, null, null)
                    btnColon.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.colon_tablet), null, null, null)
                    return Money.EURO.name
                }
            }
        }
        return ""
    }

}