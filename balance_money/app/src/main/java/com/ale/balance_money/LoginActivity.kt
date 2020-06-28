package com.ale.balance_money


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    var callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


    /**
     * This function show Alert
     */
    private fun showAlert(message:String){
        val builder = AlertDialog.Builder(this);
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }
    /**
     * method onclick when user make click in button with facebook
     *
     */
    fun loginFacebook(view: View) {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    loginResult?.let {
                        val token = it.accessToken
                        val credential = FacebookAuthProvider.getCredential(token.token)
                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                            if (it.isSuccessful){
                                val person:Person =  Person(it.result?.user?.displayName.toString(),it.result?.user?.email.toString())
                                if(person.writeNewUser(Authentication.FACEBOOK)){
                                    openMenu()
                                }else{
                                    showAlert("Hubo problemas al guardar su información, intentalo nuevamente")
                                }

                            }else {
                                showAlert("Se ha producido un error de autenticaciòn con el usuario");
                            }
                        }
                    }
                }
                override fun onCancel() {
                    Toast.makeText(applicationContext,"Cancel", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: FacebookException) {
                    showAlert("Se ha producido un error de autenticación con el usuario")
                }
            })
    }

    /**
     * This function show menu of the application
     */
    private fun openMenu(){
        val intentMenu = Intent(this, MenuActivity::class.java)
        startActivity(intentMenu)
        Animatoo.animateSlideLeft(this);
    }

    /**
     * This function execute when user wants to create a new account
     */
    fun createNewAccount(view: View) {
        val intentMenu = Intent(this, CreateAccountActivity::class.java)
        startActivity(intentMenu)
        Animatoo.animateSlideLeft(this);
    }



    fun login(view: View) {
        var email = findViewById<EditText>(R.id.txtEmail)
        var password = findViewById<EditText>(R.id.txtPassword)
        var mAuth:FirebaseAuth = FirebaseAuth.getInstance();
        val person=Person()
       var passwordEncrypted =  person.getHash(password.text.toString())
        mAuth.signInWithEmailAndPassword(email.text.toString(), passwordEncrypted.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intentMenu = Intent(this, MenuActivity::class.java)
                    startActivity(intentMenu)
                    Animatoo.animateSlideLeft(this);
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    // updateUI(null)
                    // ...
                }

                // ...
            }
    }


}