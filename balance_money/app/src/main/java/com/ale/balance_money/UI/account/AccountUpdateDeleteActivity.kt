package com.ale.balance_money.UI.account

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.R
import com.ale.balance_money.logic.Account
import com.ale.balance_money.logic.Money


class AccountUpdateDeleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_update_delete)
        val intentSpecificAccount = intent // gets the previously created intent
        var account = Account()
        account.title = intentSpecificAccount.getStringExtra("title")
        account.money = intentSpecificAccount.getStringExtra("money")
        account.amount = intentSpecificAccount.getDoubleExtra("amount", 0.0)
        account.description = intentSpecificAccount.getStringExtra("description")
        var indicatorColon = findViewById<ImageView>(R.id.imgIndicatorColon)
        var indicatorDollar = findViewById<ImageView>(R.id.imgIndicatorDollar)
        var indicatorEuro = findViewById<ImageView>(R.id.imgIndicatorEuro)
        var btnColon = findViewById<Button>(R.id.btnColon)
        var btnDollar = findViewById<Button>(R.id.btnDollar)
        var btnEuro = findViewById<Button>(R.id.btnEuro)
        var txtName = findViewById<EditText>(R.id.txtTitleAccount)
        var txtAmount = findViewById<EditText>(R.id.txtAmount)
        var txtDescription = findViewById<EditText>(R.id.txtDescription)
        account.setMoney(account.money, indicatorColon, indicatorDollar, indicatorEuro)
        account.setDataAccountUI(txtName,txtAmount,txtDescription)
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
            account.money  = Money.EURO.name
            indicatorEuro.visibility = View.VISIBLE
            indicatorDollar.visibility = View.INVISIBLE
            indicatorColon.visibility = View.INVISIBLE;
        }


    }
}