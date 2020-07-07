package com.ale.balance_money.UI.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ale.balance_money.R
import com.ale.balance_money.UI.account.AccountActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val btnAccount = findViewById<Button>(R.id.btnAccount)
      //method onclick to enter in account
        btnAccount.setOnClickListener{
            val intentAccount  =  Intent(this,
                AccountActivity::class.java)
            startActivity(intentAccount)
            Animatoo.animateSlideLeft(this);
        }
    }
}