package com.ale.balance_money.UI.transaction


import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.EditAccountPersonalActivity
import com.ale.balance_money.R
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.account.Money
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.logic.transaction.Transaction
import com.ale.balance_money.repository.FirebaseTransaction
import com.blogspot.atifsoftwares.animatoolib.Animatoo

/**
 * Detail transaction for show and user can remove
 * @author Alejandro Alvarado
 */
class DetailTransactionActivity : AppCompatActivity() {
    private val transaction = Transaction()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_transaction)
        val intentDetailTransaction = intent
        //set data in object transaction
        this.transaction.id = intentDetailTransaction.getStringExtra("id")
        this.transaction.account = intentDetailTransaction.getStringExtra("account")
        this.transaction.category = intentDetailTransaction.getStringExtra("category")
        this.transaction.amount = intentDetailTransaction.getDoubleExtra("amount", 0.0)
        this.transaction.money = intentDetailTransaction.getStringExtra("money")
        this.transaction.typeTransaction = intentDetailTransaction.getStringExtra("typeTransaction")
        this.transaction.description = intentDetailTransaction.getStringExtra("description")
        this.transaction.dateOfTrasaction = intentDetailTransaction.getStringExtra("date")
        //init element UI
        val btnAccount = findViewById<Button>(R.id.btnAccount)
        val btnCategory = findViewById<Button>(R.id.btnCategory)
        val btnMoney = findViewById<Button>(R.id.btnMoney)
        val btnAmount = findViewById<Button>(R.id.btnAmount)
        val btnTypeTransaction = findViewById<Button>(R.id.btnTypeTransaction)
        val btnDate = findViewById<Button>(R.id.btnDateTransaction)
        val txtDescription = findViewById<TextView>(R.id.txtDescriptionTransaction)
        val labelDescription = findViewById<TextView>(R.id.txtLabelDescriptionTransaction)
        //Set data to UI
        btnAccount.text = this.transaction.account
        btnCategory.text = this.transaction.category
        val icon = this.transaction.getIconMonet(this.transaction.money)
        btnAmount.text = icon + this.transaction.amount.toString()
        val drawableMoney = this.getResource()
        btnMoney.setCompoundDrawablesWithIntrinsicBounds(drawableMoney, null, null, null)
        btnMoney.text = this.transaction.money.toLowerCase().capitalize()
        btnTypeTransaction.text = this.transaction.typeTransaction
        btnDate.text = this.transaction.dateOfTrasaction
        if (this.transaction.description == "") {
            labelDescription.visibility = View.INVISIBLE
            (txtDescription.parent as ViewManager).removeView(txtDescription)
        } else {
            txtDescription.text = this.transaction.description
            txtDescription.isEnabled = false
        }

    }

    /**
     * This function getResource with icon of money to show in the button
     * @return Drawable
     */
    private fun getResource(): Drawable {
        val orientation = Device().detectTypeDevice(windowManager)
        if (orientation) {
            return when (this.transaction.money) {
                Money.COLON.name -> {
                    resources.getDrawable(R.drawable.colon)
                }
                Money.DOLLAR.name -> {
                    resources.getDrawable(R.drawable.dollar)
                }
                else -> {
                    resources.getDrawable(R.drawable.euro)
                }
            }
        } else {
            return when (this.transaction.money) {
                Money.COLON.name -> {
                    resources.getDrawable(R.drawable.colon_tablet)
                }
                Money.DOLLAR.name -> {
                    resources.getDrawable(R.drawable.dollar_tablet)
                }
                else -> {
                    resources.getDrawable(R.drawable.euro_tablet)
                }
            }
        }
    }

    /**
     * This function put menu for DetailTransactionActivity
     * @param menu
     * @return Boolean
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val provider = Device().getProviderUser()
        val inflater = menuInflater
        if(provider == Authentication.BASIC.name){
            inflater.inflate(R.menu.list_setting, menu)
        }else{
            inflater.inflate(R.menu.list_setting_without_personal_information, menu)
        }
       return true
    }

    /**
     * Is execute if user select icon for delete a detail transaction
     * @param item
     * @return Boolean
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        return when (id) {
            R.id.action_item_one -> {
                if (Device().isNetworkConnected(this)) {
                    dialogConfirmationAction(R.string.message_dialog_delete_transaction)
                } else {
                    Device().messageMistakeSnack(
                        "Para eliminar una transacción, debes estar conectado a internet",this.window.decorView.findViewById(android.R.id.content)
                    )
                }

                true
            }
            R.id.logout -> {
                startActivity(Person().singOut())
                Animatoo.animateSlideRight(this);
                finish()
                true
            }
            R.id.editPersonalInformation -> {
                //edit user
                val intentUpdateInformationUser = Intent(this, EditAccountPersonalActivity::class.java)
                startActivity(intentUpdateInformationUser)
                Animatoo.animateSlideLeft(this);
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This function show dialog to user, if user would like make a action
     * @param messageToShow
     * @param typeAction
     * @return Void
     */
    private fun dialogConfirmationAction(messageToShow: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.messageDialogTitle)
        builder.setMessage(messageToShow)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Si") { dialogInterface, which ->
            if (FirebaseTransaction().removeTransaction(transaction.id)) {
                //success
                val intentTransaction = Intent(this,TransactionActivity::class.java)
                startActivity(intentTransaction)
                Animatoo.animateSlideRight(this)
                finish()
            } else {
                //problem with delete that transaction
                Device().messageMistakeSnack(
                    "No se pudo eliminar la transacción, intentelo nuevamente",
                    window.decorView.rootView
                )
            }
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intentTransaction = Intent(this,TransactionActivity::class.java)
        startActivity(intentTransaction)
        Animatoo.animateSlideRight(this)
        finish()
    }
}

