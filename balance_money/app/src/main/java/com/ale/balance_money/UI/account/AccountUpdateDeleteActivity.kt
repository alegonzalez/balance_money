package com.ale.balance_money.UI.account

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.R
import com.ale.balance_money.logic.Account
import com.ale.balance_money.logic.Money
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class AccountUpdateDeleteActivity : AppCompatActivity() {
    var account = Account()
    lateinit var txtName: EditText
    lateinit var txtAmount: EditText
    lateinit var txtDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_update_delete)
        val intentSpecificAccount = intent
         //set data carry from ListAccountTablet.OnItemClick
        account.id = intentSpecificAccount.getStringExtra("id")
        account.title = intentSpecificAccount.getStringExtra("title")
        account.money = intentSpecificAccount.getStringExtra("money")
        account.amount = intentSpecificAccount.getDoubleExtra("amount", 0.0)
        account.description = intentSpecificAccount.getStringExtra("description")
        //set attribute of UI EditText,ImageView and Button
        var indicatorColon = findViewById<ImageView>(R.id.imgIndicatorColon)
        var indicatorDollar = findViewById<ImageView>(R.id.imgIndicatorDollar)
        var indicatorEuro = findViewById<ImageView>(R.id.imgIndicatorEuro)
        var btnColon = findViewById<Button>(R.id.btnColon)
        var btnDollar = findViewById<Button>(R.id.btnDollar)
        var btnEuro = findViewById<Button>(R.id.btnEuro)
        txtName = findViewById<EditText>(R.id.txtTitleAccount)
        txtAmount = findViewById<EditText>(R.id.txtAmount)
        txtDescription = findViewById<EditText>(R.id.txtDescription)
        var btnRemove = findViewById<FloatingActionButton>(R.id.btnRemove)
        var btnUpdate = findViewById<FloatingActionButton>(R.id.btnUpdate)
        //call function setMoney to set in UI money by user
        account.setMoney(account.money, indicatorColon, indicatorDollar, indicatorEuro)
        //call function to set all data of account in UI
        account.setDataAccountUI(txtName, txtAmount, txtDescription)
        //Onclick of button colon
        btnColon.setOnClickListener {
            account.money = Money.COLON.name
            indicatorColon.visibility = View.VISIBLE;
            indicatorDollar.visibility = View.INVISIBLE
            indicatorEuro.visibility = View.INVISIBLE
        }
        //Onclick of button dollar
        btnDollar.setOnClickListener {
            account.money = Money.DOLLAR.name
            indicatorDollar.visibility = View.VISIBLE
            indicatorColon.visibility = View.INVISIBLE;
            indicatorEuro.visibility = View.INVISIBLE
        }
        //Onclick of button euro
        btnEuro.setOnClickListener {
            account.money = Money.EURO.name
            indicatorEuro.visibility = View.VISIBLE
            indicatorDollar.visibility = View.INVISIBLE
            indicatorColon.visibility = View.INVISIBLE;
        }
        //Onclick remove button
        btnRemove.setOnClickListener {
            dialogConfirmationAction(R.string.messageDialogDelete, "remove")

        }
        //Onclick update button
        btnUpdate.setOnClickListener {
            dialogConfirmationAction(R.string.messageDialogUpdate, "update")
        }

    }
    /**
     * This function show dialog to user, if user would like make a action
     * @return Boolean
     */
    private fun dialogConfirmationAction(messageToShow: Int, typeAction: String) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(R.string.messageDialogTitle)
        //set message for alert dialog
        builder.setMessage(messageToShow)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        //performing positive action
        builder.setPositiveButton("Si") { dialogInterface, which ->
            if (typeAction == "remove") {
                if (account.removeAccount()) {
                    val intentListAccount = Intent(this, AccountActivity::class.java)
                    startActivity(intentListAccount)
                    Animatoo.animateSlideRight(this);
                } else {
                    account.messageMistakeSnack(
                        "No se pudo eliminar la cuenta correctamente, intentelo nuevamente",
                        txtDescription
                    )
                }
            } else {
                var selectMoney = findViewById<TextView>(R.id.textSelectMoney)
                if (account.validateFieldsAccount(txtName, account.money, txtAmount, selectMoney)) {
                    account.title = txtName.text.toString()
                    account.amount = txtAmount.text.toString().toDouble()
                    account.description = txtDescription.text.toString()
                    if (account.updateAccount(account)) {
                        // the account have been updated successful
                        account.messageSuccessfulSnack(
                            "La cuenta se actualizó correctamente",
                            txtDescription
                        )
                    } else {
                        // problem when try remove account
                        account.messageSuccessfulSnack(
                            "No se pudo actualizar la cuenta correctamente, intentelo nuevamente",
                            txtDescription
                        )
                    }
                }
            }
        }

        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    /**
     * method when user back the previous activity, I do animation between activities
     */
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this);
    }
}