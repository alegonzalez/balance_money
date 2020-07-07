package com.ale.balance_money.UI.login


import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ale.balance_money.R
import com.ale.balance_money.UI.menu.MenuActivity
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    var callbackManager: CallbackManager = CallbackManager.Factory.create()
    private val RC_SIGN_IN = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var buttonGoogle = findViewById<Button>(R.id.btnSignInGoogle)
        var buttonFacebook = findViewById<LoginButton>(R.id.loginBtnFacebook)
        var txtCreateNewAccount = findViewById<TextView>(R.id.txtCreateNewAccount)
        txtCreateNewAccount.paintFlags = txtCreateNewAccount.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        buttonFacebook.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(
            R.drawable.facebook
        ), null, null, null);
        buttonFacebook.setPermissions("public_profile","email")
        buttonGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, RC_SIGN_IN)
        }
        buttonFacebook.setOnClickListener{
            buttonFacebook.registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) {
                        loginResult?.let {
                            val token = it.accessToken
                            val credential = FacebookAuthProvider.getCredential(token.token)
                            authenticationProvider(Authentication.FACEBOOK, credential)
                        }
                    }
                    override fun onCancel() {}

                    override fun onError(exception: FacebookException) {
                        showAlert("Se ha producido un error de autenticación con el usuario")
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential: AuthCredential =
                        GoogleAuthProvider.getCredential(account.idToken, null)
                    this.authenticationProvider(Authentication.GOOGLE, credential)
                }
            } catch (e: ApiException) {
                showAlert("Error al obtener la cuenta")
            }

        }
    }

    /**
     * This function make authentication of different providers and save data in firebase
     */
    private fun authenticationProvider(
        typeAuthentication: Authentication,
        credential: AuthCredential
    ) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val person = Person(
                    it.result?.user?.displayName.toString(),
                    it.result?.user?.email.toString(), "", typeAuthentication.name
                )
                if (person.writeNewUser(typeAuthentication)) {
                    openMenu()
                } else {
                    showAlert("Hubo problemas al guardar su información, intentalo nuevamente")
                }
            } else {
                showAlert("Hubo problemas al guardar su información, intentalo nuevamente")
            }
        }

    }

    /**
     * This function show Alert
     */
    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this);
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * This function show menu of the application
     */
    private fun openMenu() {
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


    /**
     * This function login with password and email
     */
    fun login(view: View) {
        var email = findViewById<EditText>(R.id.txtEmail)
        var password = findViewById<EditText>(R.id.txtPassword)
        var mAuth: FirebaseAuth = FirebaseAuth.getInstance();
        val person = Person()
        var passwordEncrypted = person.getHash(password.text.toString())
        mAuth.signInWithEmailAndPassword(email.text.toString(), passwordEncrypted.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intentMenu = Intent(this, MenuActivity::class.java)
                    startActivity(intentMenu)
                    Animatoo.animateSlideLeft(this);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}