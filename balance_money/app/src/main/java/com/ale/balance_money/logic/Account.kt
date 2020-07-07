package com.ale.balance_money.logic

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
enum class Money{
    COLON,DOLLAR,EURO
}

class Account {
    var title:String = ""
    var money:String = ""
    var amount:Double = 0.0
    var description:String = ""



    /**
     * This function create new account and save in firebase
     */
    fun createNewAccount(account:Account){
        var uid = this.getUidUser()
        val ref = FirebaseDatabase.getInstance().reference
        ref.child("account").child(uid.toString()).push().setValue(account)
    }

    /**
     * This function get uid of user authenticated
     */
    fun getUidUser(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }
    /**
     * This function validate that field aren't empty
     */
    fun validateFieldsAccount(txtTitle: TextView?, typeMoney: String, txtAmount: TextView?, selectMoney: TextView?): Boolean {
      var status = false;
       if(txtTitle != null && txtAmount != null && selectMoney != null){
           if(txtTitle.text.toString() == ""){
               txtTitle.error = "El campo nombre es requerido"
               status = true
           }
           if(txtAmount.text.toString() == ""){
               txtAmount.error = "El campo monto es requerido"
               status = true
           }
           if(typeMoney == ""){
               status = true
               val snack =
                   Snackbar.make(selectMoney,
                       "Debes seleccionar el tipo de moneda",
                       Snackbar.LENGTH_SHORT
                   )
               val sandbarView: View = snack.view
               sandbarView.setBackgroundColor(Color.parseColor("#f44336"))
                snack.show()
           }
       }else{
           return false
       }
        return !status
    }
}