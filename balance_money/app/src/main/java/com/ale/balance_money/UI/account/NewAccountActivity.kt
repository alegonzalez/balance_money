package com.ale.balance_money.UI.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.R
import com.ale.balance_money.logic.Account
import com.ale.balance_money.logic.Money
import com.blogspot.atifsoftwares.animatoolib.Animatoo



class NewAccountActivity : AppCompatActivity() {
    var typeMoney:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)
        var txtDescription = findViewById<TextView>(R.id.txtDescription)
        var txtTitle = findViewById<TextView>(R.id.txtTitleAccount)
        var txtAmount = findViewById<TextView>(R.id.txtAmount)
        var btnColon = findViewById<Button>(R.id.btnColon)
        var btnDollar = findViewById<Button>(R.id.btnDollar)
        var btnEuro = findViewById<Button>(R.id.btnEuro)
        var btnCreateNewAccount = findViewById<Button>(R.id.btnNewAccount)
        var selectMoney = findViewById<TextView>(R.id.textSelectMoney)
        var indicatorColon = findViewById<ImageView>(R.id.imgIndicatorColon)
        var indicatorDollar = findViewById<ImageView>(R.id.imgIndicatorDollar)
        var indicatorEuro = findViewById<ImageView>(R.id.imgIndicatorEuro)
        //Onclick of button colon
        btnColon.setOnClickListener{
            typeMoney = Money.COLON.name
            indicatorColon.visibility = View.VISIBLE;
            indicatorDollar.visibility = View.INVISIBLE
            indicatorEuro.visibility = View.INVISIBLE
        }
        //Onclick of button dollar
        btnDollar.setOnClickListener{
            typeMoney = Money.DOLLAR.name
            indicatorDollar.visibility = View.VISIBLE
            indicatorColon.visibility = View.INVISIBLE;
            indicatorEuro.visibility = View.INVISIBLE
        }
        //Onclick of button euro
        btnEuro.setOnClickListener{
            typeMoney = Money.EURO.name
            indicatorEuro.visibility = View.VISIBLE
            indicatorDollar.visibility = View.INVISIBLE
            indicatorColon.visibility = View.INVISIBLE;
        }
        //onclick of button to create new account
        btnCreateNewAccount.setOnClickListener{
           val account = Account()
            if(account.validateFieldsAccount(txtTitle,typeMoney,txtAmount,selectMoney)){
                var account:Account = Account()
                account.title = txtTitle.text.toString()
                account.money = typeMoney
                account.amount = txtAmount.text.toString().toDouble()
                account.description = txtDescription.text.toString()
                account.createNewAccount(account)
                var intentAccount = Intent(this,AccountActivity::class.java)
                startActivity(intentAccount)
                Animatoo.animateSlideRight(this);
            }
        }
    }

    /**
     * method when user back the previous activity, I do animation between activities
     */
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this);
    }
}