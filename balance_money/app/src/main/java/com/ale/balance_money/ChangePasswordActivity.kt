package com.ale.balance_money

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.ale.balance_money.UI.menu.MenuActivity
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.repository.FirebaseUser
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : AppCompatActivity() {
    var isPasswordVisible: Boolean = false
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        val oldPassword = findViewById<EditText>(R.id.txtOldPassword)
        val newPassword = findViewById<EditText>(R.id.txtNewPassword)
        val confirmPassword = findViewById<EditText>(R.id.txtConfirmPassword)
        val btnUpdatePassword = findViewById<Button>(R.id.btnUpdatePassword)
        //click in icon field for password to  show or hide password
        oldPassword.setOnTouchListener(View.OnTouchListener { v, event ->
            showHidePassword(event, oldPassword)
        })
        //click in icon field for password to  show or hide password
        newPassword.setOnTouchListener(View.OnTouchListener { v, event ->
            showHidePassword(event, newPassword)
        })
        //click in icon field for password to  show or hide password
        confirmPassword.setOnTouchListener(View.OnTouchListener { v, event ->
            showHidePassword(event, confirmPassword)
        })
        //onclick for update password
        btnUpdatePassword.setOnClickListener {
            var user = Person()
            if (user.validatePassword(oldPassword.text.toString())) {
                oldPassword.error = "El campo contraseña actual es requerido"
            } else if (user.validatePassword(newPassword.text.toString())) {
                newPassword.error = "El campo contraseña nueva es requerido"
            } else if (user.validatePassword(confirmPassword.text.toString())) {
                confirmPassword.error = "El campo confirmar contraseña es requerido"

            } else {
                user = Device().getUser()
                val passwordDecrypt = user.decodePassword(user.password)
                if (!user.checkPassowrdWithPasswordTypeByUser(
                        passwordDecrypt,
                        oldPassword.text.toString()
                    )
                ) {
                    oldPassword.error = "La contraseña de la cuenta es incorrecta"
                } else {
                    val result = user.checkPassword(
                        newPassword.text.toString(),
                        confirmPassword.text.toString()
                    )
                    if(result!= "0" && result!= "1"){
                        //update password
                        user.password = result
                       if(FirebaseUser().updatePasswordUser(user)){
                           val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()
                           val currentUser = FirebaseAuth.getInstance().currentUser
                           user.saveSharepreferen(
                               prefs,
                               user.name,
                               user.email,
                               user.password,
                               Authentication.BASIC.name,
                               currentUser?.uid
                           )
                           //currentUser?.updatePassword(newPassword.text.toString())
                           currentUser?.updatePassword(newPassword.text.toString())
                           Device().messageSuccessfulSnack("Se ha cambiado la contraseña correctamente",newPassword)
                       }else{
                           Device().messageMistakeSnack("La contraseña no se pudo cambiar, intentelo nuevamente",newPassword)
                       }
                    }else{
                        newPassword.error = "El campo nueva contraseña no coincide con el campo confirmar contraseña"
                    }
                }
            }
        }
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
