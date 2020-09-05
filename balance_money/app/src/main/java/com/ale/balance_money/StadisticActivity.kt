package com.ale.balance_money

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.setting.Device
import com.blogspot.atifsoftwares.animatoolib.Animatoo

/**
 * Stadistic activity
 * @author Alejandro Alvarado
 */
class StadisticActivity : AppCompatActivity() {
    lateinit var configStadistic: Fragment
    lateinit var graphic: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stadistic)
        val device = Device()
        val typeDevice = Device().detectTypeDevice(windowManager)
        configStadistic = ConfigStadisticFragment()
        if(!device.isNetworkConnected(this)){
            device.messageMistakeSnack("Para visualizar el gráfico con los resultados, debes estar conectado a internet",window.decorView)
        }
        if (savedInstanceState == null) {
            //call fragment for select dates
            supportFragmentManager.beginTransaction().add(R.id.containerFragment, configStadistic)
                .commit()
            checkDevice(typeDevice)
        }
    }

    /**
     * Check if type device is a tablet for load grafic, for load both fragment
     * @param typeDevice
     * @return void
     */
    private fun checkDevice(typeDevice:Boolean) {
        //detect if type device
        if (!typeDevice) {
            graphic = GraphicFragment()
            supportFragmentManager.beginTransaction().add(R.id.containerGrapficFragment, graphic)
                .commit()
        }

    }
    /**
     * This function put menu for DetailTransactionActivity
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