package com.ale.balance_money.logic

import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

enum class Money {
    COLON, DOLLAR, EURO
}

class Account {
    var id: String = ""
    var title: String = ""
    var money: String = ""
    var amount: Double = 0.0
    var description: String = ""


    /**
     * This function create new account and save in firebase
     */
    fun createNewAccount(account: Account) {
        var uid = this.getUidUser()
        val ref = getDatabaseReference()
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
    fun validateFieldsAccount(
        txtTitle: TextView?,
        typeMoney: String,
        txtAmount: TextView?,
        selectMoney: TextView?
    ): Boolean {
        var status = false;
        if (txtTitle != null && txtAmount != null && selectMoney != null) {
            if (txtTitle.text.toString() == "") {
                txtTitle.error = "El campo nombre es requerido"
                status = true
            }
            if (txtAmount.text.toString() == "") {
                txtAmount.error = "El campo monto es requerido"
                status = true
            }
            if (typeMoney == "") {
                status = true
                messageMistakeSnack("Debes seleccionar el tipo de moneda", selectMoney)
            }
        } else {
            return false
        }
        return !status
    }



    /**
     * This function set name, amount and description of account
     */
    fun setDataAccountUI(txtName: EditText?, txtAmount: EditText?, txtDescription: EditText?) {
        txtName?.setText(this.title)
        txtAmount?.setText(this.amount.toString())
        txtDescription?.setText(this.description)
    }

    /**
     * this function update detail of account
     */
    fun updateAccount(account: Account): Boolean {
        return try {
            val uid = this.getUidUser()
            var ref = this.getDatabaseReference()
            ref.child("account").child(uid.toString()).child(account.id).setValue(account);
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * this function show message successful of any action
     */
    fun messageSuccessfulSnack(message: String, view: View?) {
        if(view!= null){
            val snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            snack.show()
        }

    }

    /**
     * this function show message when happen a mistake
     */
    fun messageMistakeSnack(message: String, view: View) {

           val snack =
               Snackbar.make(
                   view,
                   message,
                   Snackbar.LENGTH_SHORT
               )
           val sandbarView: View = snack.view
           sandbarView.setBackgroundColor(Color.parseColor("#f44336"))
           snack.show()

   }

    /**
     *this function remove a specific account from firebase
     * @return void
     */
    fun removeAccount(): Boolean {
        return try {
            val uid = this.getUidUser()
            val ref = this.getDatabaseReference()
            ref.child("account").child(uid.toString()).child(this.id).removeValue();
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     *this function get database reference of firebase
     * return DatabaseReference
     */
    private fun getDatabaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }
}