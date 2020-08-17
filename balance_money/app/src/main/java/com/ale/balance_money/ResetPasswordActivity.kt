package com.ale.balance_money

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ale.balance_money.UI.login.LoginActivity
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.transaction.Transaction
import com.ale.balance_money.model.PersonViewModel
import com.ale.balance_money.model.TransactionViewModel
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import kotlinx.android.synthetic.main.activity_transaction.*

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
            val listAccount = person.getAccountOfUser()
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
        if (person.checkAccountUser(viewModelPerson.listAccountuser as ArrayList<Person>, email,mDialog)) {
            if (person.resetPassword(email, mDialog)) {
                val intentLogin = Intent(this, LoginActivity::class.java)
                startActivity(intentLogin)
                Animatoo.animateSlideRight(this);
                finish();
            }
        }
    }

    /**
     * This function start dialog waiting
     */
    private fun startDialog() {
        mDialog.setMessage("Espere un momento por favor...")
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
    }
}