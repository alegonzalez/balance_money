package com.ale.balance_money.UI.transaction

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ale.balance_money.R
import com.ale.balance_money.logic.ExchangeRate
import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.logic.account.Money
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.logic.transaction.Transaction
import com.ale.balance_money.model.AccountViewModel
import com.ale.balance_money.model.CategoryViewModel
import com.ale.balance_money.repository.FirebaseData
import com.ale.balance_money.repository.FirebaseTransaction
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import kotlinx.android.synthetic.main.activity_create_new_transaction.*


/**
 * new transaction
 * @author Alejandro Alvarado
 */
class CreateNewTransactionActivity : AppCompatActivity() {
    private val viewModelAccount by lazy { ViewModelProvider(this).get(AccountViewModel::class.java) }
    private val viewModelCategories by lazy { ViewModelProvider(this).get(CategoryViewModel::class.java) }
    private var listAccount = listOf<String>()
    private var listCategories = listOf<String>()
    private var transaction: Transaction = Transaction()
    private var account = AccountMoney()
    private lateinit var progressDialog: ProgressDialog
    private var money: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.show()
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.setCanceledOnTouchOutside(false);
        setContentView(R.layout.activity_create_new_transaction)
        //field UI
        val dropdownAccount = findViewById<Spinner>(R.id.dropdownAccount)
        val dropDownCategory = findViewById<Spinner>(R.id.dropdownCategory)
        val amountTransaction = findViewById<EditText>(R.id.editAmount)
        val totalAmount = findViewById<TextView>(R.id.txtTotalAmount)
        val groupRadio = findViewById<RadioGroup>(R.id.rdGroup)
        val btnColon = findViewById<Button>(R.id.btnColon)
        val btnDollar = findViewById<Button>(R.id.btnDollar)
        val btnEuro = findViewById<Button>(R.id.btnEuro)
        val txtDescription = findViewById<EditText>(R.id.txtDescriptionTransaction)
        val btnCreateNewTransaction = findViewById<Button>(R.id.btnNewTransaction)
        val orientation = Device().detectTypeDevice(windowManager)

        if (viewModelAccount.listAccount == null && viewModelCategories.listCategories == null) {
            observeAccount(dropdownAccount, dropDownCategory)
        } else {
            this.listAccount = Transaction().fillListAccount(viewModelAccount.listAccount)
            fillDropdownAccount(dropdownAccount)
            this.listCategories =
                Transaction().fillListCategories(viewModelCategories.listCategories)
            fillDropdownCategory(dropDownCategory)
        }

        //event when user has  selected  an account
        dropdownAccount.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val nameAccount = dropdownAccount.getItemAtPosition(position).toString();
                val listData = transaction.getAmountByAccountSelected(
                    viewModelAccount.listAccount,
                    nameAccount
                )
                val iconMoney = transaction.getIconMonet(listData.get(0).money)
                totalAmount.text = "El monto de la cuenta es: ${iconMoney}${listData.get(0).amount}"
                transaction.money = listData.get(0).money
                //set new amount of account
                money = transaction.money
                transaction.remainingAmount = listData.get(0).amount
                //set data of account to object AccountMoney
                account.money = listData.get(0).money
                account.amount = listData.get(0).amount
                account.title = listData.get(0).title
                account.description = listData.get(0).description
                account.id = listData.get(0).id
                account.setMoney(transaction.money, orientation, btnColon, btnDollar, btnEuro)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
        //event when user has  seleted  a category
        dropDownCategory.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                transaction.category = dropDownCategory.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
        //onclick if user select money colon for transaction
        //call function setMoney money of account
        btnColon.setOnClickListener {
            transaction.money = Money.COLON.name
            account.setMoney(transaction.money, orientation, btnColon, btnDollar, btnEuro)
        }
        //onclick if user select money dollar for transaction
        btnDollar.setOnClickListener {
            transaction.money = Money.DOLLAR.name
            account.setMoney(transaction.money, orientation, btnColon, btnDollar, btnEuro)
        }
        //onclick if user select money euro for transaction
        btnEuro.setOnClickListener {
            transaction.money = Money.EURO.name
            account.setMoney(transaction.money, orientation, btnColon, btnDollar, btnEuro)
        }
        groupRadio.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val rb = findViewById<View>(checkedId) as RadioButton
            transaction.typeTransaction = rb.text.toString()
        })
        btnCreateNewTransaction.setOnClickListener {
            if (Device().isNetworkConnected(this)) {
                this.transaction.description = txtDescription.text.toString()
                dialogConfirmationAction(
                    R.string.message_dialo_create_new_transaction,
                    amountTransaction
                )
            } else {
                Device().messageMistakeSnack(
                    "Para crear una transacción, debes estar conectado a internet",
                    amountTransaction
                )
            }

        }
    }

    /**
     * this functon is a observable to get all account by user
     * @param dropdownAccount
     * @param dropdownCategory
     */
    private fun observeAccount(dropdownAccount: Spinner, dropdownCategory: Spinner) {
        viewModelAccount.fetchAccount().observe(this, Observer { listAccount ->
            viewModelAccount.listAccount = listAccount
            this.listAccount = Transaction().fillListAccount(listAccount)
            fillDropdownAccount(dropdownAccount)
        })
        viewModelCategories.getListCategories().observe(this, Observer { listCategories ->
            viewModelCategories.listCategories = listCategories
            this.listCategories = Transaction().fillListCategories(listCategories)
            fillDropdownCategory(dropdownCategory)
        })
    }

    /**
     * This function fill dropdown account
     * @param dropdownAccount
     * return void
     */
    private fun fillDropdownAccount(dropdownAccount: Spinner) {
        val messageAccount = arrayOf("No hay cuentas")
        setElementInSpinner(listAccount, messageAccount, dropdownAccount)
    }

    /**
     * This function set data in spinner when device is smartphone or tablet
     */
    private fun setElementInSpinner(
        list: List<String>,
        emptyList: Array<String>,
        dropdown: Spinner
    ) {
        val orientation = Device().detectTypeDevice(windowManager)
        val adapter: ArrayAdapter<String>
        if (orientation) {
            if (list.isEmpty()) {
                adapter = ArrayAdapter(this, R.layout.spinner_dropdown_layout, emptyList)
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            } else {
                adapter = ArrayAdapter(this, R.layout.spinner_dropdown_layout, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner);

            }
            dropdown.adapter = adapter
        } else {
            if (list.isEmpty()) {
                adapter = ArrayAdapter(this, R.layout.spinner_dropdown_tablet, emptyList)
                adapter.setDropDownViewResource(R.layout.custom_spinner_tablet);
            } else {
                adapter = ArrayAdapter(this, R.layout.spinner_dropdown_tablet, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_tablet);
            }
            dropdown.adapter = adapter
        }

        if (viewModelCategories.listCategories != null && viewModelAccount.listAccount != null) {
            progressDialog.dismiss()
        }
    }

    /**
     * This function fill dropdown account
     * @param dropdownCategory
     * return void
     */
    private fun fillDropdownCategory(dropdownCategory: Spinner) {
        val messageCategory = arrayOf("No hay categorias")
        setElementInSpinner(listCategories, messageCategory, dropdownCategory)
    }

    /**
     * This function show dialog to user, if user would like make a action
     * @param messageToShow
     * @param typeAction
     * @return Void
     */
    private fun dialogConfirmationAction(messageToShow: Int, amountTransaction: EditText) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(R.string.messageDialogTitle)
        //set message for alert dialog
        builder.setMessage(messageToShow)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        //performing positive action
        builder.setPositiveButton("Si") { dialogInterface, which ->
            createNewTransaction(amountTransaction)
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
     * this function is executed when user make click yes in Dialog confimation
     * @param amountTransaction
     */
    fun createNewTransaction(amountTransaction: EditText) {
        transaction.amount =
            if (amountTransaction.text.toString() == "") 0.0 else amountTransaction.text.toString()
                .toDouble()
        transaction.dateOfTrasaction = transaction.getDateNow()
        transaction.account = account.title
        if (money != transaction.money) {
            val exchangeRate = ExchangeRate()
            exchangeRate.execute(transaction.money, money, transaction.amount.toString())
            transaction.amount = exchangeRate.get()
            account.amount =
                if (transaction.typeTransaction == "Ingreso") transaction.remainingAmount + transaction.amount else transaction.remainingAmount - transaction.amount
        } else {
            account.amount =
                if (transaction.typeTransaction == "Ingreso") transaction.remainingAmount + transaction.amount else transaction.remainingAmount - transaction.amount
        }
        val listError = transaction.validateFieldToMakeTransaction(transaction)
        if (listError.get(0) == "" && listError.get(1) == "") {
            //insert in firebase new transactio
            if (FirebaseTransaction().insertNewTransaction(transaction)) {
                FirebaseData().updateAccount(account)
                Device().messageSuccessfulSnack(
                    "La transacción se creo correctamente",
                    txtNameAccountTransaction
                )
                val intentTransaction = Intent(this, TransactionActivity::class.java)
                startActivity(intentTransaction)
                Animatoo.animateSlideRight(this)
                finish()
            } else {
                Device().messageMistakeSnack(
                    "No se pudo crear la transacción, intentelo nuevamente",
                    txtNameAccountTransaction
                )
            }
        } else {
            if (listError.get(0) != "") {
                amountTransaction.error = listError.get(0)
            }
            if (listError.get(1) != "") {
                Device().messageMistakeSnack(listError.get(1), rdGroup)
            }
        }
    }

    /**
     * method when user back the previous activity, I do animation between activities
     */
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this)
        finish()
    }
}