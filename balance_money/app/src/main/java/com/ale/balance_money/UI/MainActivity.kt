package com.ale.balance_money.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.R
import com.ale.balance_money.UI.login.LoginActivity
import com.ale.balance_money.UI.menu.MenuActivity
import com.ale.balance_money.logic.Person
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth





class MainActivity : AppCompatActivity() {
    private val person = Person()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setTheme(R.style.AppTheme)

        val handler: Handler = Handler()
        handler.postDelayed({
            checkLogin()
        }, 5000)

    }

    /**
     * This function if user is login or not
     */
    private fun checkLogin() {
       val logIn = person.isLoggedIn()
        val accountGoogle = GoogleSignIn.getLastSignedInAccount(this)

        if (logIn) {
            openMenu()
        } else if (accountGoogle != null) {
         // val idUser =   accountGoogle.
            val test = accountGoogle.id.toString()
          val p = FirebaseAuth.getInstance().getCurrentUser()?.getUid();
            openMenu()
        } else if (!logIn) {
            val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE)
            val email = prefs.getString("email", null)
            val provider = prefs.getString("provider", null)
            if (email != null && provider != null) {
                openMenu()
            } else {
                val intentLogin = Intent(this, LoginActivity::class.java)
                startActivity(intentLogin)
                Animatoo.animateFade(this);
                finish();
            }
        }
    }

    /**
     * This function show menu of app
     */
    private fun openMenu() {
        val intentMenu = Intent(this, MenuActivity::class.java)
        startActivity(intentMenu)
        Animatoo.animateSlideLeft(this);
        finish();
    }

    override fun onDestroy() {
        finish()
        super.onDestroy()
    }
}