package com.ale.balance_money.UI.login


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.R
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth

/**
 * this class is for create new personal account
 * @author Alejandro Alvarado
 */
class CreateAccountActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        val btnCreateNewAccount = findViewById<Button>(R.id.btnCrearNewAccount)
        mAuth = FirebaseAuth.getInstance();
        var  progressBar = findViewById<ProgressBar>(R.id.progressBar)
        var name = findViewById<EditText>(R.id.txtName)
        var email = findViewById<EditText>(R.id.txtEmail)
        var password = findViewById<EditText>(R.id.txtPassword)
        val confirmPassword = findViewById<EditText>(R.id.txtConfirmPassword)

        //onclick for create new personal account
        btnCreateNewAccount.setOnClickListener{
            val person= Person()
            var statusValidation = false
            if( name.text.toString() == ""){
                name.error = "El campo nombre es requerido"
                statusValidation = true
            }
            if(person.checkEmail(email.text.toString())){
                email.error = "El campo email es requerido"
                statusValidation = true
            }else if(!person.validateEmail(email.text.toString())){
                email.error = "Correo electrónico inválido"
                statusValidation = true
            }
            when {
                person.checkPassword(password.text.toString(),confirmPassword.text.toString()) == "0" -> {
                    password.error = "El campo contraseña es requerido"
                    confirmPassword.error = "El campo confirmar contraseña es requerido"
                    statusValidation = true
                }
                person.checkPassword(password.text.toString(),confirmPassword.text.toString()) == "1" -> {
                    password.error = "El campo contraseña y confirmar contraseña no coinciden"
                    statusValidation = true

                }
                else -> {
                    person.password=  person.checkPassword(password.text.toString(),confirmPassword.text.toString())
                }
            }
            if(!statusValidation){
                person.email = email.text.toString()
                person.name = name.text.toString()
                authenticationEmailPassword(progressBar,person.email,person.password,person)


            }
        }
    }

    /**
     * This function make authentication with firebase with email and password
     * @param progressBar
     * @param email
     * @param password
     * @param person
     * @return void
     */
    private fun authenticationEmailPassword(progressBar:ProgressBar,email:String,password:String,person: Person){
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    person.writeNewUser(Authentication.BASIC)
                    progressBar?.visibility = View.GONE
                    val intent =
                        Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    Animatoo.animateSlideRight(this);
                } else {
                    Toast.makeText(
                        applicationContext,
                        "El usuario que intenta registrar ya existe!!",
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar?.visibility = View.GONE
                }
            }
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