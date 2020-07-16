package com.ale.balance_money.UI.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.R
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
        val indicatorColon = findViewById<ImageView>(R.id.imgIndicatorColon)
        val indicatorDollar = findViewById<ImageView>(R.id.imgIndicatorDollar)
        val indicatorEuro = findViewById<ImageView>(R.id.imgIndicatorEuro)
        //Onclick of button colon
        btnColon.setOnClickListener {
            typeMoney = Money.COLON.name
            indicatorColon.visibility = View.VISIBLE;
            indicatorDollar.visibility = View.INVISIBLE
            indicatorEuro.visibility = View.INVISIBLE
        }
        //Onclick of button dollar
        btnDollar.setOnClickListener {
            typeMoney = Money.DOLLAR.name
            indicatorDollar.visibility = View.VISIBLE
            indicatorColon.visibility = View.INVISIBLE;
            indicatorEuro.visibility = View.INVISIBLE
        }
        //Onclick of button euro
        btnEuro.setOnClickListener {
            typeMoney = Money.EURO.name
            indicatorEuro.visibility = View.VISIBLE
            indicatorDollar.visibility = View.INVISIBLE
            indicatorColon.visibility = View.INVISIBLE;
        }
        //onclick of button to create new account
        btnCreateNewAccount.setOnClickListener {
            val account = AccountMoney()
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
                FirebaseData().createNewAccount(account)
                val intentAccount = Intent(this, AccountActivity::class.java)
                startActivity(intentAccount)
                Animatoo.animateSlideRight(this);
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
     * method when user back the previous activity, I do animation between activities
     */
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this);
        finish()
    }
}