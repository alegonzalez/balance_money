package com.ale.balance_money.UI.menu

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.R
import com.ale.balance_money.StadisticActivity
import com.ale.balance_money.UI.account.AccountActivity
import com.ale.balance_money.UI.category.CategoryActivity
import com.ale.balance_money.UI.transaction.TransactionActivity
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.setting.Device
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth


class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val btnAccount = findViewById<Button>(R.id.btnAccount)
        val btnCategory = findViewById<Button>(R.id.btnCategory)
        val btnTransaction = findViewById<Button>(R.id.btnTransaction)
        val btnStadistic = findViewById<Button>(R.id.btnStadistic)
        //method onclick to enter in account
        btnAccount.setOnClickListener {
            val intentAccount = Intent(
                this,
                AccountActivity::class.java
            )
            startActivity(intentAccount)
            Animatoo.animateSlideLeft(this);
        }
        //method onclick to enter in category
        btnCategory.setOnClickListener {
            val intentCategory = Intent(this, CategoryActivity::class.java)
            startActivity(intentCategory)
            Animatoo.animateSlideLeft(this);
        }
        //method onclick to enter in transaction
        btnTransaction.setOnClickListener {
            val intentTransaction = Intent(this, TransactionActivity::class.java)
            startActivity(intentTransaction)
            Animatoo.animateSlideLeft(this);
        }
        //method onclick to enter in transaction
        btnStadistic.setOnClickListener {
            val intentStadistic = Intent(this, StadisticActivity::class.java)
            startActivity(intentStadistic)
            Animatoo.animateSlideLeft(this);
        }

    }

    /**
     * This function put menu for menu
     * @param menu
     * @return Boolean
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val provider = Person().getProviderUser()
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
                    //logout user
                    startActivity(Person().singOut())
                    Animatoo.animateSlideRight(this);
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
                true
            }
            else -> super.onOptionsItemSelected(item)
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