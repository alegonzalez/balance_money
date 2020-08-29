package com.ale.balance_money.UI.login


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ale.balance_money.R
import com.ale.balance_money.ResetPasswordActivity
import com.ale.balance_money.UI.menu.MenuActivity
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.model.PersonViewModel
import com.ale.balance_money.repository.FirebaseUser
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

/**
 * This class show all provider for authentication
 * @author Alejandro Alvarado
 */
class LoginActivity : AppCompatActivity() {
    var isPasswordVisible: Boolean = false
    private var callbackManager: CallbackManager = CallbackManager.Factory.create()
    private val viewModelPerson by lazy { ViewModelProvider(this).get(PersonViewModel::class.java) }
    lateinit var mDialog: ProgressDialog
    private val RC_SIGN_IN = 100



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mDialog = ProgressDialog(this)
        val buttonGoogle = findViewById<Button>(R.id.btnSignInGoogle)
        val buttonFacebook = findViewById<LoginButton>(R.id.loginBtnFacebook)
        val txtCreateNewAccount = findViewById<TextView>(R.id.txtCreateNewAccount)
        val resetPassword = findViewById<TextView>(R.id.txtForgetPassword)
        val password = findViewById<EditText>(R.id.txtPassword)
        txtCreateNewAccount.paintFlags = txtCreateNewAccount.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        resetPassword.paintFlags = resetPassword.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        buttonFacebook.setCompoundDrawablesWithIntrinsicBounds(
            resources.getDrawable(R.drawable.facebook),
            null,
            null,
            null
        )
        buttonFacebook.compoundDrawablePadding = 25;
        buttonFacebook.setPermissions("public_profile", "email")

        //onclick login with google
        buttonGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()


            val googleClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, RC_SIGN_IN)
        }

        //onclick login with facebook
        buttonFacebook.setOnClickListener {
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
                        Device().messageMistakeSnack("No se pudo autenticarse con facebook, por favor intentelo nuevamente", window.decorView.rootView)
                    }
                })
        }
        //click in icon field for password to  show or hide password
        password.setOnTouchListener(View.OnTouchListener { v, event ->
            showHidePassword(event, password)
        })
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
                Device().messageMistakeSnack("No se pudo obtener la cuenta, por favor intentalo nuevamente", window.decorView.rootView)
            }
        }
    }

    /**
     * This function make authentication of different providers and save data in firebase
     * @param typeAuthentication
     * @param credential
     * @return void
     */
    private fun authenticationProvider(
        typeAuthentication: Authentication,
        credential: AuthCredential
    ) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val person = Person(
                    it.result?.user?.displayName.toString(),
                    it.result?.user?.email.toString(),
                    "",
                    typeAuthentication.name
                )
                if (FirebaseUser().writeNewUser(typeAuthentication,person)) {
                    val uidUser = FirebaseAuth.getInstance().currentUser?.uid
                    val prefs = getSharedPreferences(
                        getString(R.string.pref_file),
                        Context.MODE_PRIVATE
                    ).edit()
                    person.saveSharepreferen(
                        prefs,
                        person.name,
                        person.email,
                        "",
                        person.provider,
                        uidUser
                    )
                    openMenu()
                } else {
                    Device().messageMistakeSnack("Hubo problemas al guardar su información, intentalo nuevamente", window.decorView.rootView)
                }
            } else {
                Device().messageMistakeSnack("Hubo problemas al guardar su información, intentalo nuevamente", window.decorView.rootView)
            }
        }

    }

    /**
     * this functon is a observable get account user with email,password,name and provider
     * @return void
     */
    private fun observeAccountUser(user:Person) {
        viewModelPerson.getAccountUser().observe(this, Observer { accountUser ->
            viewModelPerson.listAccountuser = accountUser
            saveDataAccount(user)
            mDialog.dismiss()
        })
    }

    /**
     * This function show menu of the application
     * @return void
     */
    private fun openMenu() {
        val intentMenu = Intent(this, MenuActivity::class.java)
        startActivity(intentMenu)
        Animatoo.animateSlideLeft(this);
    }

    /**
     * Onclick when user wants to create a new account
     * @param view
     * @return void
     */
    fun openActivityCreateNewAccountUser(view: View) {
        val intentMenu = Intent(this, CreateAccountActivity::class.java)
        startActivity(intentMenu)
        Animatoo.animateSlideLeft(this)
    }

    /**
     * Onclick when user wants to reset password because is forgot
     * @param view
     * @return void
     */
    fun openActivityResetPassword(view: View) {
        val intentResetPassword = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intentResetPassword)
        Animatoo.animateSlideLeft(this);
    }

    /**
     * This function login with password and email
     */
    fun login(view: View) {
        val person = Person()
        val email = findViewById<EditText>(R.id.txtEmail)
        val password = findViewById<EditText>(R.id.txtPassword)
        if (person.checkEmail(email.text.toString()) && person.validatePassword(password.text.toString())) {
            email.error = "El campo correo es requerido"
            password.error = "El campo contraseña es requerido"
        } else if (person.checkEmail(email.text.toString())) {
            email.error = "El campo correo es requerido"
        } else if (person.validatePassword(password.text.toString())) {
            password.error = "El campo contraseña es requerido"
        } else if (!person.validateEmail(email.text.toString())) {
            email.error = "El correo es invalido"

        } else {

            val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
            val passwordEncrypted = person.getHash(password.text.toString())
            person.password = passwordEncrypted.toString()
            mAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if (viewModelPerson.listAccountuser == null) {
                            this.startDialog()
                            this.observeAccountUser(person)
                        } else {
                            this.startDialog()
                            saveDataAccount(person)
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                      Device().messageMistakeSnack("Autenticación incorrecta, por favor intentelo nuevamente",window.decorView.rootView)
                    }
                }.addOnFailureListener(this) { exception: Exception ->
                    Device().messageMistakeSnack(
                        "Autenticación incorrecta, por favor intentelo nuevamente",
                        window.decorView.rootView
                    )
                }

        }
    }

    /**
     * save personal information about user
     * @param user
     */
    fun saveDataAccount(user: Person) {
        val prefs = getSharedPreferences(
            getString(R.string.pref_file),
            Context.MODE_PRIVATE
        ).edit()
        val uidUser = FirebaseAuth.getInstance().currentUser?.uid
        user.saveSharepreferen(
            prefs,
            viewModelPerson.listAccountuser!![0].name,
            viewModelPerson.listAccountuser!![0].email,
            user.password,
            Authentication.BASIC.name,
            uidUser
        )
        openMenu()
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
        Animatoo.animateSlideRight(this);
        finish();
        System.exit(0);
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}