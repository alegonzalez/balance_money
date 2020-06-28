package com.ale.balance_money

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo


class MainActivity : AppCompatActivity() {
    private val person = Person()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val handler:Handler =  Handler()
        handler.postDelayed({
            checkLogin()
        }, 5000)

    }

    /**
     * This function if user is login or not
     */
    private fun checkLogin(){
        if(person.isLoggedIn()){

            val intentMenu = Intent(this, MenuActivity::class.java)
            startActivity(intentMenu)
            Animatoo.animateSlideLeft(this);

             /*
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
            Animatoo.animateSlideLeft(this);

              */
        }else{
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
            Animatoo.animateZoom(this);
        }
    }

    override fun onDestroy() {
        finish()
        super.onDestroy()
    }
}