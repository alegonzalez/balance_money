package com.ale.balance_money.UI.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.ale.balance_money.R
import com.ale.balance_money.logic.category.Category
import com.ale.balance_money.logic.setting.Device
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

            //event click to create new category
            btnCreateNewCategory.setOnClickListener {
            val category = Category()
            category.name = txtName.text.toString()
            category.description = txtDescription.text.toString()
            if (category.validateField()) {
                FirebaseDataCategory().insertNewCategory(category)
                Device().messageSuccessfulSnack(
                    "La categoria " + txtName.text.toString() + " se creo con exito",
                    txtName
                )
            } else {
                txtName.error = "El campo nombre es requerido"
            }
        }
    }


    //This function make animation when user back activity
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this);
    }
}