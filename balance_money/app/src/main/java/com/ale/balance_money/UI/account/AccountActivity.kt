package com.ale.balance_money.UI.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.R
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


}


