package com.ale.balance_money

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ale.balance_money.logic.setting.Device

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
        val typeDevice = Device().detectTypeDevice(windowManager)
        configStadistic = ConfigStadisticFragment()
        if(!Device().isNetworkConnected(this)){
            Device().messageMistakeSnack("Para visualizar el gráfico con los resultados, debes estar conectado a internet",window.decorView)
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
    fun checkDevice(typeDevice:Boolean) {
        //detect if type device
        if (!typeDevice) {
            graphic = GraphicFragment()
            supportFragmentManager.beginTransaction().add(R.id.containerGrapficFragment, graphic)
                .commit()
        }

    }
}