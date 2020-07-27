package com.ale.balance_money.UI.account


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.R
import com.ale.balance_money.logic.ExchangeRate
import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.logic.account.Money
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.repository.FirebaseData
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * class activity where for delete and update detail of account
 * @author Alejandro Alvarado
 */
class AccountUpdateDeleteActivity : AppCompatActivity() {
    var account = AccountMoney()
    lateinit var txtName: EditText
    lateinit var txtAmount: EditText
    lateinit var txtDescription: EditText
    var typeMoney = ""
    var device = Device()
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
        val indicatorColon = findViewById<ImageView>(R.id.imgIndicatorColon)
        val indicatorDollar = findViewById<ImageView>(R.id.imgIndicatorDollar)
        val indicatorEuro = findViewById<ImageView>(R.id.imgIndicatorEuro)
        val btnColon = findViewById<Button>(R.id.btnColon)
        val btnDollar = findViewById<Button>(R.id.btnDollar)
        val btnEuro = findViewById<Button>(R.id.btnEuro)
        txtName = findViewById<EditText>(R.id.txtTitleAccount)
        txtAmount = findViewById<EditText>(R.id.txtAmount)
        txtDescription = findViewById<EditText>(R.id.txtDescription)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        //call function setMoney to set in UI money by user
        val orientation = Device().detectTypeDevice(windowManager)
        typeMoney = setMoney(account.money, orientation,btnColon, btnDollar, btnEuro)
        //Set data in UI
        txtName.setText(account.title)
        txtAmount.setText(account.amount.toString())
        txtDescription.setText(account.description)

        //Onclick of button colon
        btnColon.setOnClickListener {
            account.money = Money.COLON.name
            setMoney(account.money, orientation,btnColon, btnDollar, btnEuro)
        }
        //Onclick of button dollar
        btnDollar.setOnClickListener {
            account.money = Money.DOLLAR.name
            setMoney(account.money, orientation,btnColon, btnDollar, btnEuro)
        }
        //Onclick of button euro
        btnEuro.setOnClickListener {
            account.money = Money.EURO.name
            setMoney(account.money, orientation,btnColon, btnDollar, btnEuro)
        }
        //Onclick update button
        btnUpdate.setOnClickListener {

            val listError: ArrayList<String> = account.validateFieldsAccount(
                txtName.text.toString(),
                account.money,
                txtAmount.text.toString()
            )
            if (listError[0] == "" && listError[1] == "" && listError[2] == "") {
                account.title = txtName.text.toString()
                account.amount = txtAmount.text.toString().toDouble()
                account.description = txtDescription.text.toString()
                dialogConfirmationAction(R.string.messageDialogUpdate, "update")
            } else {
                val selectMoney = findViewById<TextView>(R.id.textSelectMoney)
                if (listError[0] != "") {
                    txtName.error = listError[0]
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
     * @param typeAction
     * @return Void
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
            val firebaseData = FirebaseData()
            if (typeAction == "remove") {
                if (firebaseData.removeAccount(account.id)) {
                    val intentListAccount = Intent(this, AccountActivity::class.java)
                    startActivity(intentListAccount)
                    Animatoo.animateSlideRight(this);
                } else {
                    // problem when try update an account
                    device.messageMistakeSnack(
                        "No se pudo eliminar la cuenta correctamente, intentelo nuevamente",
                        txtDescription
                    )
                }
            } else {
                if(account.money != typeMoney){
                    val exchangeRate = ExchangeRate()
                    exchangeRate.execute(typeMoney,account.money,account.amount.toString())
                    account.amount = exchangeRate.get()
                }
                 if (firebaseData.updateAccount(account)) {
                    // the account have been updated successful
                    device.messageSuccessfulSnack(
                        "La cuenta se actualizó correctamente",
                        txtDescription
                    )
                } else {
                    // problem when try remove an account
                    device.messageSuccessfulSnack(
                        "No se pudo actualizar la cuenta correctamente, intentelo nuevamente",
                        txtDescription
                    )
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
        Animatoo.animateSlideRight(this)
        finish()
    }

    /**
     * This function set visibility money that was selected by user
     * @param money
     * @param indicatorColon
     * @param indicatorDollar
     * @param indicatorEuro
     */
    private fun setMoney(
        money: String?,
        orientation:Boolean,
        btnColon:Button,
        btnDollar:Button,
        btnEuro:Button
    ): String {
        if(orientation){
            //Smartphone
            if(money == Money.COLON.name ){
                btnColon.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.colon), null, resources.getDrawable(R.drawable.check), null);
                btnEuro.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.euro), null, null, null)
                btnDollar.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.dollar), null, null, null)
                return Money.COLON.name
            }else if(money == Money.DOLLAR.name  ){
                btnDollar.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.dollar),null,resources.getDrawable(R.drawable.check),null);
                btnEuro.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.euro), null, null, null)
                btnColon.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.colon), null, null, null)
                return Money.DOLLAR.name
            }else if(money == Money.EURO.name){
                btnEuro.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.euro), null, resources.getDrawable(R.drawable.check), null)
                btnDollar.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.dollar), null, null, null)
                btnColon.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.colon), null, null, null)
                return Money.EURO.name
            }
        }else{
            //Tablet
            if(money == Money.COLON.name ){
                btnColon.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.colon_tablet), null, resources.getDrawable(R.drawable.check_tablet), null);
                btnEuro.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.euro_tablet), null, null, null)
                btnDollar.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.dollar_tablet), null, null, null)
                return Money.COLON.name
            }else if(money == Money.DOLLAR.name  ){
                btnDollar.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.dollar_tablet),null,resources.getDrawable(R.drawable.check_tablet),null);
                btnEuro.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.euro_tablet), null, null, null)
                btnColon.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.colon_tablet), null, null, null)
                return Money.DOLLAR.name
            }else if(money == Money.EURO.name){
                btnEuro.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.euro_tablet), null, resources.getDrawable(R.drawable.check_tablet), null)
                btnDollar.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.dollar_tablet), null, null, null)
                btnColon.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.colon_tablet), null, null, null)
                return Money.EURO.name
            }
        }
        return ""
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        // return true so that the menu pop up is opened
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        return if (id == R.id.action_item_one) {
            dialogConfirmationAction(R.string.messageDialogDelete, "remove")
            true
        } else super.onOptionsItemSelected(item)
    }
}