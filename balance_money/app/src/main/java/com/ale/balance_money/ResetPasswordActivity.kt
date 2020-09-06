package com.ale.balance_money

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ale.balance_money.UI.login.LoginActivity
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.model.PersonViewModel
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var mDialog: ProgressDialog
    val person = Person()
    lateinit var email: EditText
    private val viewModelPerson by lazy { ViewModelProvider(this).get(PersonViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        mDialog = ProgressDialog(this)
        email = findViewById<EditText>(R.id.txtResetEmail)
        val btnResetEmail = findViewById<Button>(R.id.btnResetPassword)
        //onclick for reset password
        btnResetEmail.setOnClickListener {
            btnResetEmail.isEnabled = false
            if (person.checkEmail(email.text.toString())) {
                email.error = "El campo correo es requerido"
            } else if (!person.validateEmail(email.text.toString())) {
                email.error = "El formato del correo el incorrecto"
            } else {
                //check if categories list is null to call observeAccount and Fill list or set in recyclerview
                if (viewModelPerson.listAccountuser == null) {
                    this.startDialog()
                    this.observeAccountUser()
                } else {
                    this.startDialog()
                    this.verificateEmail()
                }
            }
            btnResetEmail.isEnabled = true
        }
    }

    /**
     * this functon is a observable to get all category by user
     * @return void
     */
    private fun observeAccountUser() {
        viewModelPerson.getListAccountUser().observe(this, Observer { listAccountUser ->
            if (listAccountUser.isEmpty()) {
                email.error = "El correo ingresado no se encuentra registrado"
                mDialog.dismiss()
            } else {
                viewModelPerson.listAccountuser = listAccountUser
                this.verificateEmail()
            }
        })
    }

    /**
     * this function call function in class person for check if email typed by user exist and is Basic provider
     */
    private fun verificateEmail() {

        if (checkAccountUser(viewModelPerson.listAccountuser as ArrayList<Person>, email,mDialog)) {
            if (resetPassword(email, mDialog)) {
                val intentLogin = Intent(this, LoginActivity::class.java)
                startActivity(intentLogin)
                Animatoo.animateSlideRight(this);
                finish();
            }
        }


    }
    /**
     * this function send email for reset password of user
     * @param email
     * @return Boolean
     */
    fun resetPassword(email: EditText, mDialog: ProgressDialog): Boolean {
        val mAuth = FirebaseAuth.getInstance()
        //mAuth.setLanguageCode("es")
        mAuth.useAppLanguage()
        var state = false
        mAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener {
            if (it.isSuccessful) {
                Device().messageSuccessfulSnack("El correo se ha enviando correctamente", email)
                state = true
            } else {
                Device().messageMistakeSnack(
                    "El correo no se ha enviando, intentelo nuevamente",
                    email
                )
            }
            mDialog.dismiss()
        }
        return state
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
     * this function check if user has an account and doesn't authenticated by  google and facebook, only basic authentication
     * @param listAccount
     * @param email
     * @return Boolean
     */
    fun checkAccountUser(
        listAccount: ArrayList<Person>,
        email: EditText?,
        mDialog: ProgressDialog
    ): Boolean {
        for (item in listAccount) {
            if (item.email == email?.text.toString()) {
                return when (item.provider) {
                    Authentication.BASIC.name -> {
                        true
                    }
                    Authentication.GOOGLE.name -> {
                        email?.error =
                            "El correo ingresado esta asociado a una cuenta de google, no necesita restablecer la contraseña."
                        mDialog.dismiss()
                        false
                    }
                    else -> {
                        email?.error =
                            "El correo ingresado esta asociado a una cuenta de facebook, no necesita restablecer la contraseña."
                        mDialog.dismiss()
                        false
                    }
                }
            }
        }
        mDialog.dismiss()
        email?.error = "El correo ingresado no se encuentra registrado."
        return false
    }
    /**
     * method when user back the previous activity, I do animation between activities
     */
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this)
        finish()
    }
}