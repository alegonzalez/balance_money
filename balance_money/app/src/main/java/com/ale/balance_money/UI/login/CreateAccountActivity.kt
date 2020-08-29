package com.ale.balance_money.UI.login


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
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
    var isPasswordVisible: Boolean = false
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        val btnCreateNewAccount = findViewById<Button>(R.id.btnCrearNewAccount)
        mAuth = FirebaseAuth.getInstance();
       // val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val name = findViewById<EditText>(R.id.txtName)
        val email = findViewById<EditText>(R.id.txtEmail)
        val password = findViewById<EditText>(R.id.txtPassword)
        val confirmPassword = findViewById<EditText>(R.id.txtConfirmPassword)
        password.setOnTouchListener(View.OnTouchListener { v, event ->
            showHidePassword(event, password)
        })
        confirmPassword.setOnTouchListener(View.OnTouchListener { v, event ->
            showHidePassword(event, confirmPassword)
        })
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
            var passwordDecrypted = ""
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
                    passwordDecrypted = password.text.toString()
                }
            }
            if(!statusValidation){
                person.email = email.text.toString()
                person.name = name.text.toString()
                authenticationEmailPassword(com.ale.balance_money.repository.FirebaseUser(),person,passwordDecrypted)
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
    private fun authenticationEmailPassword(firebaseUser: com.ale.balance_money.repository.FirebaseUser, person:Person, passwordDecrypted:String){
        mAuth!!.createUserWithEmailAndPassword(person.email, passwordDecrypted)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseUser.writeNewUser(Authentication.BASIC,person)
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
                }
            }
    }
    /**
     * this function is for show and hide passoword of the fields  password and confirm password
     * @param event
     * @param password
     * @return Boolean
     */
    private fun showHidePassword(event: MotionEvent?, password: EditText):Boolean {
        if (event != null) {
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= password.right - password.compoundDrawables[2].bounds.width()) {
                    val selection: Int = password.selectionEnd
                    if (isPasswordVisible) {
                        // set drawable image
                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_visibility_off_24, 0)
                        // hide Password
                        password.transformationMethod = PasswordTransformationMethod.getInstance()
                        isPasswordVisible = false
                    } else {
                        // set drawable image
                        password.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_show_password_24,0)
                        // show Password
                        password.transformationMethod = HideReturnsTransformationMethod.getInstance()
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
        Animatoo.animateSlideRight(this)
        finish()
    }
}