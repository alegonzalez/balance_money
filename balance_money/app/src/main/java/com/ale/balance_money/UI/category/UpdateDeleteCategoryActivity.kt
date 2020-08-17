package com.ale.balance_money.UI.category

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.R
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.category.Category
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.repository.FirebaseDataCategory
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UpdateDeleteCategoryActivity : AppCompatActivity() {
    var category = Category()
    lateinit var nameCategory:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_delete_category)
        val intentDetailCategory = intent
        category.id = intentDetailCategory.getStringExtra("id")
        category.name = intentDetailCategory.getStringExtra("name")
        category.description = intentDetailCategory.getStringExtra("description")

         nameCategory = findViewById<EditText>(R.id.txtNameCategoryDetail)
        val descriptionCategory = findViewById<EditText>(R.id.txtDescriptionCategoryDetail)
        val buttonUpdate = findViewById<Button>(R.id.btnUpdateCategory)
        nameCategory.setText(category.name)
        descriptionCategory.setText(category.description)
        //event onclick fot update detail category
        buttonUpdate.setOnClickListener {
            if(Device().isNetworkConnected(this)){
                category.name = nameCategory.text.toString()
                category.description = descriptionCategory.text.toString()
                dialogConfirmationActionCategory(R.string.messageDialogUpdateCategory,"update",nameCategory)
            }else{
                Device().messageMistakeSnack("Para actualizar la información de una categorías, debes estar conectado a internet",nameCategory)
            }
        }
    }

    /**
     * This function show dialog to user, if user would like make a action
     */
    private fun dialogConfirmationActionCategory(messageToShow: Int, typeAction: String,nameCategory:EditText) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(R.string.messageDialogTitle)
        //set message for alert dialog
        builder.setMessage(messageToShow)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        //performing positive action
        builder.setPositiveButton("Si") { dialogInterface, which ->
            val firebaseCategory = FirebaseDataCategory()
            val view: View = this.window.decorView.findViewById(android.R.id.content)
            if (typeAction == "remove") {
                  if(firebaseCategory.removeCategory(category.id)){
                      val intentMainCategory = Intent(this,CategoryActivity::class.java)
                      startActivity(intentMainCategory)
                      Animatoo.animateSlideRight(this);
                  }else{
                        Device().messageMistakeSnack("La categoría no se puedo eliminar correctamente",
                          view
                      )
                  }
            } else {
                if(category.validateField()){
                    val device = Device()
                    if(firebaseCategory.updateCategory(category)){
                        device.messageSuccessfulSnack("La categoría se actualizo correctamente",view)
                    }else{
                        device.messageMistakeSnack("La categoría no se pudo actualizar correctamente, intentelo nuevamente",view)
                    }
                }else{
                    nameCategory.error = "El campo nombre es requerido"
                }
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
    /**
     * method when user back the previous activity, I do animation between activities
     */
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this)
        finish()
    }
    /**
     * This function put menu for delete or update a category
     * @param menu
     * @return Boolean
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val provider = Person().getProviderUser()
        val inflater = menuInflater
        if(provider == Authentication.BASIC.name){
            inflater.inflate(R.menu.list_setting, menu)
        }else{
            inflater.inflate(R.menu.list_setting_without_personal_information, menu)
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
                true
            }
            R.id.action_item_one ->{
                if(Device().isNetworkConnected(this)){
                    dialogConfirmationActionCategory(R.string.messageDialogDeleteCategory,"remove",nameCategory)
                }else{
                    Device().messageMistakeSnack("Para eliminar una categorías, debes estar conectado a internet",nameCategory)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}