package com.ale.balance_money.UI.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.ListAccountTablet
import com.ale.balance_money.R
import com.ale.balance_money.logic.Device
import com.blogspot.atifsoftwares.animatoolib.Animatoo



class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setSupportActionBar(findViewById(R.id.toolbar))
      if (Device().detectTypeDevice(this.windowManager)) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container,
                    ListAccountFragment()
                ).commit()
        }else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListAccountTablet()).commit()
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


