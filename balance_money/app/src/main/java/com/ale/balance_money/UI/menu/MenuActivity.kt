package com.ale.balance_money.UI.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ale.balance_money.R
import com.ale.balance_money.StadisticActivity
import com.ale.balance_money.UI.account.AccountActivity
import com.ale.balance_money.UI.category.CategoryActivity
import com.ale.balance_money.UI.transaction.TransactionActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val btnAccount = findViewById<Button>(R.id.btnAccount)
        val btnCategory = findViewById<Button>(R.id.btnCategory)
        val btnTransaction = findViewById<Button>(R.id.btnTransaction)
        val btnStadistic = findViewById<Button>(R.id.btnStadistic)
      //method onclick to enter in account
        btnAccount.setOnClickListener{
            val intentAccount  =  Intent(this,
                AccountActivity::class.java)
            startActivity(intentAccount)
            Animatoo.animateSlideLeft(this);
        }
        //method onclick to enter in category
        btnCategory.setOnClickListener{
            val intentCategory  =  Intent(this, CategoryActivity::class.java)
            startActivity(intentCategory)
            Animatoo.animateSlideLeft(this);
        }
        //method onclick to enter in transaction
        btnTransaction.setOnClickListener{
            val intentTransaction =  Intent(this, TransactionActivity::class.java)
            startActivity(intentTransaction)
            Animatoo.animateSlideLeft(this);
        }
        //method onclick to enter in transaction
        btnStadistic.setOnClickListener{
            val intentStadistic =  Intent(this, StadisticActivity::class.java)
            startActivity(intentStadistic)
            Animatoo.animateSlideLeft(this);
        }
    }
}