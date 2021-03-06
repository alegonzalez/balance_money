package com.ale.balance_money.UI.account

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.EditAccountPersonalActivity
import com.ale.balance_money.R
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.logic.account.Money
import com.ale.balance_money.repository.FirebaseData
import com.blogspot.atifsoftwares.animatoolib.Animatoo


/**
 * this class is for create a new account
 * @author Alejandro Alvarado
 */
class NewAccountActivity : AppCompatActivity() {
    private var typeMoney: String = ""
    private lateinit var txtTitle: EditText
    private lateinit var txtAmount: EditText
    var device = Device()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)
        val txtDescription = findViewById<TextView>(R.id.txtDescription)
        txtTitle = findViewById(R.id.txtTitleAccount)
        txtAmount = findViewById(R.id.txtAmount)
        val btnColon = findViewById<Button>(R.id.btnColon)
        val btnDollar = findViewById<Button>(R.id.btnDollar)
        val btnEuro = findViewById<Button>(R.id.btnEuro)
        val btnCreateNewAccount = findViewById<Button>(R.id.btnNewAccount)
        val orientation = Device().detectTypeDevice(windowManager)
        val account = AccountMoney()
        if(!device.isNetworkConnected(this)){
            device.messageMistakeSnack("Para crear una cuenta, debes estar conectado a internet",txtTitle)
        }
        //Onclick of button colon
        btnColon.setOnClickListener {
            typeMoney = Money.COLON.name
            account.setMoney(typeMoney,orientation,btnColon,btnDollar,btnEuro)
            btnColon.compoundDrawablePadding = 12
        }
        //Onclick of button dollar
        btnDollar.setOnClickListener {
            typeMoney = Money.DOLLAR.name
            account.setMoney(typeMoney,orientation,btnColon,btnDollar,btnEuro)
            btnDollar.compoundDrawablePadding = 12
        }
        //Onclick of button euro
        btnEuro.setOnClickListener {
            typeMoney = Money.EURO.name
            account.setMoney(typeMoney,orientation,btnColon,btnDollar,btnEuro)
            btnEuro.compoundDrawablePadding = 12
        }
        //onclick of button to create new account
        btnCreateNewAccount.setOnClickListener {
            val listError: ArrayList<String> = account.validateFieldsAccount(
                txtTitle.text.toString(),
                typeMoney,
                txtAmount.text.toString()
            )
            if (listError[0] == "" && listError[1] == "" && listError[2] == "") {
                account.title = txtTitle.text.toString()
                account.money = typeMoney
                account.amount = txtAmount.text.toString().toDouble()
                account.description = txtDescription.text.toString()
                dialogConfirmationAction(R.string.message_confirmation_new_account,account)

            } else {
                val selectMoney = findViewById<TextView>(R.id.textSelectMoney)
                if (listError[0] != "") {
                    txtTitle.error = listError[0]
                }
                if (listError[1] != "") {
                    device.messageMistakeSnack(listError[1], selectMoney)
                }
                if (listError[2] != "") {
                    txtAmount.error = listError[2]
                }
            }
        }
    }
    /**
     * This function show dialog to user, if user would like make a action
     * @param messageToShow
     * @param account
     * @return Void
     */
    private fun dialogConfirmationAction(messageToShow: Int,account:AccountMoney) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.messageDialogTitle)
        builder.setTitle(R.string.messageDialogTitle)
        builder.setMessage(messageToShow)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        //performing positive action
        builder.setPositiveButton("Si") { dialogInterface, which ->
            if(device.isNetworkConnected(this)){
                FirebaseData().createNewAccount(account)
                Animatoo.animateSlideRight(this)
                finish()
            }else{
                device.messageMistakeSnack("El dispositivo no se encuentra conectado a internet",txtTitle)
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
        finish()
    }
    /**
     * This function put menu for category
     * @param menu
     * @return Boolean
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val provider = Device().getProviderUser()
        val inflater = menuInflater
        if (provider == Authentication.BASIC.name) {
            inflater.inflate(R.menu.menu, menu)
        } else {
            inflater.inflate(R.menu.menu_without_personal_information, menu)
        }
        return true
    }

    /**
     * Is execute if user select setting option
     * @param item
     * @return Boolean
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        return when (id) {
            R.id.logout -> {
                if (Device().isNetworkConnected(this)) {
                    //logut user
                    startActivity(Person().singOut())
                    Animatoo.animateSlideRight(this)
                    finish()
                } else {
                    Device().messageMistakeSnack(
                        "Para salir de tu usuario , debes estar conectado a internet",
                        this.window.decorView.findViewById(android.R.id.content)
                    )
                }

                true
            }
            R.id.editPersonalInformation -> {
                //edit user
                val intentUpdateInformationUser = Intent(this, EditAccountPersonalActivity::class.java)
                startActivity(intentUpdateInformationUser)
                Animatoo.animateSlideLeft(this)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}