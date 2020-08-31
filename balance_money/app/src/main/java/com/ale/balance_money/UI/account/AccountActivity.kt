package com.ale.balance_money.UI.account

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.EditAccountPersonalActivity
import com.ale.balance_money.R
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.setting.Device
import com.blogspot.atifsoftwares.animatoolib.Animatoo

/**
 * This class load fragment to show list of accounts
 * @author Alejandro Alvarado
 */

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setSupportActionBar(findViewById(R.id.toolbar))
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if(!Device().isNetworkConnected(this)){
            Device().messageMistakeSnack("Para ver la lista de las cuentas, debes estar conectado a internet",window.decorView)
        }
        //check if fragment is load
        if (fragment == null) {
            if (Device().detectTypeDevice(this.windowManager)) {
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.container,
                        ListAccountFragment()
                    ).commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container,
                        ListAccountTablet()
                    ).commit()
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
    /**
     * This function put menu for category
     * @param menu
     * @return Boolean
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val provider = Device().getProviderUser()
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
                    //logut user
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
                val intentUpdateInformationUser = Intent(this, EditAccountPersonalActivity::class.java)
                startActivity(intentUpdateInformationUser)
                Animatoo.animateSlideLeft(this);
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}


