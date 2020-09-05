package com.ale.balance_money.UI.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.ale.balance_money.EditAccountPersonalActivity
import com.ale.balance_money.R
import com.ale.balance_money.UI.account.AccountActivity
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.logic.category.Category
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.repository.FirebaseData
import com.ale.balance_money.repository.FirebaseDataCategory
import com.blogspot.atifsoftwares.animatoolib.Animatoo


class CreateNewCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_category)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        val txtName = findViewById<EditText>(R.id.txtNameNewCategory)
        val txtDescription = findViewById<EditText>(R.id.txtDescriptionNewCategory)
        val btnCreateNewCategory = findViewById<Button>(R.id.btnNewCategory)
        if(!Device().isNetworkConnected(this)){
            Device().messageMistakeSnack("Para crear una nueva categoría, debes estar conectado a internet",txtName)
        }
            //event click to create new category
            btnCreateNewCategory.setOnClickListener {
            val category = Category()
            category.name = txtName.text.toString()
            category.description = txtDescription.text.toString()
            if (category.validateField()) {
             dialogConfirmationAction(R.string.message_dialog_create_new_category,txtName,category)
            } else {
                txtName.error = "El campo nombre es requerido"
            }
        }
    }
    /**
     * This function show dialog to user, if user would like make a action
     * @param messageToShow
     * @param typeAction
     * @return Void
     */
    private fun dialogConfirmationAction(messageToShow: Int,txtName:EditText,category:Category) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.messageDialogTitle)
        builder.setTitle(R.string.messageDialogTitle)
        builder.setMessage(messageToShow)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        //performing positive action
        builder.setPositiveButton("Si") { dialogInterface, which ->
            if(Device().isNetworkConnected(this)){
                FirebaseDataCategory().insertNewCategory(category)
                Device().messageSuccessfulSnack(
                    "La categoría " + txtName.text.toString() + " se creo con éxito",
                    txtName
                )
            }else{
                Device().messageMistakeSnack("El dispositivo no se encuentra conectado a internet",txtName)
            }
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    //This function make animation when user back activity
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this);
        finish()
    }
    /**
     * This function put menu for new category
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