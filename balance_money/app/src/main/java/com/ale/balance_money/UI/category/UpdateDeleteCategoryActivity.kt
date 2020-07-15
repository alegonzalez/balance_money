package com.ale.balance_money.UI.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.R
import com.ale.balance_money.logic.Category.Category
import com.ale.balance_money.repository.FirebaseDataCategory
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UpdateDeleteCategoryActivity : AppCompatActivity() {
    var category = Category()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_delete_category)
        val intentDetailCategory = intent
        category.id = intentDetailCategory.getStringExtra("id")
        category.name = intentDetailCategory.getStringExtra("name")
        category.description = intentDetailCategory.getStringExtra("description")

        var nameCategory = findViewById<EditText>(R.id.txtNameCategoryDetail)
        var descriptionCategory = findViewById<EditText>(R.id.txtDescriptionCategoryDetail)
        var buttonRemove = findViewById<FloatingActionButton>(R.id.btnRemoveCategory)
        var buttonUpdate = findViewById<FloatingActionButton>(R.id.btnUpdateCategory)
        nameCategory.setText(category.name)
        descriptionCategory.setText(category.description)
        //event onclick for remove a category
        buttonRemove.setOnClickListener {
        dialogConfirmationActionCategory(R.string.messageDialogDeleteCategory,"remove",nameCategory)
        }
        //event onclick fot update detail category
        buttonUpdate.setOnClickListener {
            category.name = nameCategory.text.toString()
            category.description = descriptionCategory.text.toString()
            dialogConfirmationActionCategory(R.string.messageDialogUpdateCategory,"update",nameCategory)
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
            val account = com.ale.balance_money.logic.account.Account()
            val view: View = this.window.decorView.findViewById(android.R.id.content)
            if (typeAction == "remove") {
                  if(firebaseCategory.removeCategory(category.id)){
                      var intentMainCategory = Intent(this,CategoryActivity::class.java)
                      startActivity(intentMainCategory)
                      Animatoo.animateSlideRight(this);
                  }else{
                        account.messageMistakeSnack("La categoría no se puedo eliminar correctamente",
                          view
                      )
                  }
            } else {
                if(category.validateField()){
                    if(firebaseCategory.updateCategory(category)){
                        account.messageSuccessfulSnack("La categoría se actualizo correctamente",view)
                    }else{
                        account.messageMistakeSnack("La categoría no se pudo actualizar correctamente, intentelo nuevamente",view)
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
        Animatoo.animateSlideRight(this);
    }
}