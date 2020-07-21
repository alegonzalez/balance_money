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
import com.ale.balance_money.UI.account.AccountActivity
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
 * this class is
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

        val expense = findViewById<RadioButton>(R.id.rdButtonExpense)
        val groupRadio = findViewById<RadioGroup>(R.id.rdGroup)
        val btnColon = findViewById<Button>(R.id.btnColon)
        val btnDollar = findViewById<Button>(R.id.btnDollar)
        val btnEuro = findViewById<Button>(R.id.btnEuro)
        val indicatorColon = findViewById<ImageView>(R.id.imgIndicatorColon)
        val indicatorDollar = findViewById<ImageView>(R.id.imgIndicatorDollar)
        val indicatorEuro = findViewById<ImageView>(R.id.imgIndicatorEuro)
        val btnCreateNewTransaction = findViewById<Button>(R.id.btnNewTransaction)


        if (viewModelAccount.listAccount == null && viewModelCategories.listCategories == null) {
            observeAccount(dropdownAccount, dropDownCategory)
        } else {
            fillDropdownAccount(dropdownAccount)
            fillDropdownCategory(dropDownCategory)
        }

        //event when user has  seleted  an account
        dropdownAccount.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                var nameAccount = dropdownAccount.getItemAtPosition(position).toString();
                var listData = transaction.getAmountByAccountSelected(
                    viewModelAccount.listAccount,
                    nameAccount
                )
                totalAmount.text = "El monto de la cuenta es: ${listData.get(0).amount}"
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
                setMoney(transaction.money, indicatorColon, indicatorDollar, indicatorEuro)
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
        btnColon.setOnClickListener {
            transaction.money = Money.COLON.name
            setMoney(transaction.money, indicatorColon, indicatorDollar, indicatorEuro)
        }
        //onclick if user select money dollar for transaction
        btnDollar.setOnClickListener {
            transaction.money = Money.DOLLAR.name
            setMoney(transaction.money, indicatorColon, indicatorDollar, indicatorEuro)
        }
        //onclick if user select money euro for transaction
        btnEuro.setOnClickListener {
            transaction.money = Money.EURO.name
            setMoney(transaction.money, indicatorColon, indicatorDollar, indicatorEuro)
        }
        groupRadio.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val rb = findViewById<View>(checkedId) as RadioButton
            transaction.typeTransaction = rb.text.toString()
        })
        btnCreateNewTransaction.setOnClickListener {
            dialogConfirmationAction(
                R.string.message_dialo_create_new_transaction,
                amountTransaction
            )
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
            // progressDialog.dismiss()
        })
    }

    /**
     * This function fill dropdown account
     * @param dropdownAccount
     * return void
     */
    private fun fillDropdownAccount(dropdownAccount: Spinner) {
        val adapter: ArrayAdapter<String>
        val messageAccount = arrayOf("No hay cuentas")
        if (listAccount.isEmpty()) {
            adapter = ArrayAdapter(this, R.layout.spinner_dropdown_layout, messageAccount)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        } else {
            adapter = ArrayAdapter(this, R.layout.spinner_dropdown_layout, listAccount)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        }
        if (viewModelCategories.listCategories != null && viewModelAccount.listAccount != null) {
            progressDialog.dismiss()
        }
        dropdownAccount.adapter = adapter
    }

    /**
     * This function fill dropdown account
     * @param dropdownCategory
     * return void
     */
    private fun fillDropdownCategory(dropdownCategory: Spinner) {
        val adapter: ArrayAdapter<String>
        val messageCategory = arrayOf("No hay categorias")
        if (listCategories.isEmpty()) {
            adapter = ArrayAdapter(this, R.layout.spinner_dropdown_layout, messageCategory)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        } else {
            adapter = ArrayAdapter(this, R.layout.spinner_dropdown_layout, listCategories)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        }
        if (viewModelCategories.listCategories != null && viewModelAccount.listAccount != null) {
            progressDialog.dismiss()
        }
        dropdownCategory.adapter = adapter
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
        indicatorColon: ImageView?,
        indicatorDollar: ImageView?,
        indicatorEuro: ImageView?
    ): String {
        if (money == Money.COLON.name && indicatorColon != null) {
            indicatorColon.visibility = View.VISIBLE
            indicatorDollar?.visibility = View.INVISIBLE
            indicatorEuro?.visibility = View.INVISIBLE
            return Money.COLON.name
        } else if (money == Money.DOLLAR.name && indicatorDollar != null) {
            indicatorDollar.visibility = View.VISIBLE
            indicatorColon?.visibility = View.INVISIBLE;
            indicatorEuro?.visibility = View.INVISIBLE
            return Money.DOLLAR.name
        } else if (money == Money.EURO.name && indicatorEuro != null) {
            indicatorEuro.visibility = View.VISIBLE
            indicatorDollar?.visibility = View.INVISIBLE
            indicatorColon?.visibility = View.INVISIBLE;
            return Money.EURO.name
        }
        return ""
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

                val intentTransaction = Intent(this,TransactionActivity::class.java)
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