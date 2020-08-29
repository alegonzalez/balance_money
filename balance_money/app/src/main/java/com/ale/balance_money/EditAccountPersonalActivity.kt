package com.ale.balance_money


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ale.balance_money.UI.menu.MenuActivity
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.model.PersonViewModel
import com.ale.balance_money.repository.FirebaseUser
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth


class EditAccountPersonalActivity : AppCompatActivity() {
    var isPasswordVisible: Boolean = false
    lateinit var mDialog: ProgressDialog
    val personalInformation = Device().getUser()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account_personal)
        mDialog = ProgressDialog(this)
        val name = findViewById<EditText>(R.id.txtName)
        val email = findViewById<EditText>(R.id.txtEmail)
        val password = findViewById<EditText>(R.id.txtPassword)
        val confirmPassword = findViewById<EditText>(R.id.txtConfirmPassword)
        val btnUpdate = findViewById<Button>(R.id.btnUpdateAccountPersonal)
        this.setAccountData(name, email)
        //click in icon field for password to  show or hide password
        password.setOnTouchListener(OnTouchListener { v, event ->
            showHidePassword(event, password)
        })


        //click in icon field for confirm password to  show or hide password
        confirmPassword.setOnTouchListener(OnTouchListener { v, event ->
            showHidePassword(event, confirmPassword)
        })
        //onclick for update information personal
        btnUpdate.setOnClickListener {
            var passwordTypeByUser = password.text.toString()
            personalInformation.name = name.text.toString()
            personalInformation.email = email.text.toString()

            /**
             *  check if field password and confirm password is equal
             *  0 two field empty
             *  1 field password and confirm password is not equals
             */
            val checkPassword =
                personalInformation.checkPassword(
                    passwordTypeByUser,
                    confirmPassword.text.toString()
                )
            when (checkPassword) {
                "0" -> {
                    password.error = "El campo contraseña es requerido"
                    confirmPassword.error = "El campo confirmar contraseña es requerido"
                }
                "1" -> {
                    password.error =
                        "El campo contraseña no coincide con el campo confirmar contraseña"
                }
                else -> {
                    passwordTypeByUser = checkPassword
                    val passwordTest = personalInformation.password
                    //check if password is correct
                    if (personalInformation.password.trim() != passwordTypeByUser.trim()) {
                        password.error = "La contraseña ingresada es incorrecta"
                    } else if (personalInformation.name == "" && personalInformation.checkEmail(
                            personalInformation.email
                        )
                    ) {
                        //check field name and email are not empty
                        name.error = "El campo nombre es requerido"
                        email.error = "El campo correo es requerido"
                    } else if (personalInformation.name == "") {
                        name.error = "El campo nombre es requerido"
                    } else if (personalInformation.checkEmail(personalInformation.email)) {
                        email.error = "El campo correo es requerido"
                    } else if (!personalInformation.validateEmail(email.text.toString())) {
                        //check field email correct
                        email.error = "El correo es invalido"
                    } else {
                        this.startDialog()
                        //update information
                        if (FirebaseUser().updateAccountUser(personalInformation)) {
                            mDialog.dismiss()
                            val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            personalInformation.saveSharepreferen(
                                prefs,
                                personalInformation.name,
                                personalInformation.email,
                                personalInformation.password,
                                Authentication.BASIC.name,
                                currentUser?.uid
                            )
                            Device().messageSuccessfulSnack(
                                "Se actualizó la información correctamente",
                                name
                            )
                        } else {
                            mDialog.dismiss()
                            Device().messageMistakeSnack(
                                "No se pudo actualizar la información, intentelo nuevamente",
                                name
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * this function set data in UI
     * @param name
     * @param email
     */
    private fun setAccountData(name: EditText, email: EditText) {
        name.setText(this.personalInformation.name)
        email.setText(this.personalInformation.email)
    }

    /**
     * this function is for show and hide passoword of the fields  password and confirm password
     * @param event
     * @param password
     * @return Boolean
     */
    private fun showHidePassword(event: MotionEvent?, password: EditText): Boolean {
        if (event != null) {
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= password.right - password.compoundDrawables[2].bounds.width()) {
                    val selection: Int = password.selectionEnd
                    if (isPasswordVisible) {
                        // set drawable image
                        password.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_password_visibility_off_24,
                            0
                        )
                        // hide Password
                        password.transformationMethod = PasswordTransformationMethod.getInstance()
                        isPasswordVisible = false
                    } else {
                        // set drawable image
                        password.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_show_password_24,
                            0
                        )
                        // show Password
                        password.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                        isPasswordVisible = true
                    }
                    password.setSelection(selection)
                }
            }
        }
        return false
    }


/**
     * This function start dialog waiting
     */
    private fun startDialog() {
        mDialog.setMessage("Espere un momento por favor...")
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
    }

    /**
     * method when user back the previous activity, I do animation between activities
     */
    override fun onBackPressed() {
        super.onBackPressed()
        val intentMenu = Intent(this, MenuActivity::class.java)
        startActivity(intentMenu)
        Animatoo.animateSlideRight(this);
        finish()
    }
}