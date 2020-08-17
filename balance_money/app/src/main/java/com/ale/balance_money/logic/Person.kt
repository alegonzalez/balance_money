package com.ale.balance_money.logic


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import android.util.Base64
import com.ale.balance_money.R
import com.ale.balance_money.UI.login.LoginActivity
import com.facebook.AccessToken
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.getApplicationContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern


enum class Authentication {
    FACEBOOK, GOOGLE, BASIC
}

class Person {
    var name: String
    var email: String
    var password: String
    var provider: String

    //Constructor with all attributes of class
    constructor(name: String, email: String, password: String = "", provider: String) {
        this.name = name
        this.email = email
        this.password = password;
        this.provider = provider
    }

    //Constructor empty
    constructor() {
        this.name = ""
        this.email = ""
        this.password = ""
        this.provider = ""
    }


    /**
     * This function check if user is log in
     * @return Boolean
     */
    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null
    }

    /**
     * This function save data like email and provider
     * @param email
     * @param provider
     * @return void
     */
    fun saveSharepreferen(
        prefs: SharedPreferences.Editor,
        email: String,
        provider: String,
        uidUser: String?
    ) {
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.putString("uidUser", uidUser)
        prefs.apply()
    }

    /**
     * This function get provider of user authenticated
     * @return String
     */
    fun getProviderUser(): String? {
        val context = getApplicationContext()
        val prefs = context.getSharedPreferences(
            context.getResources().getString(R.string.pref_file),
            Context.MODE_PRIVATE
        )
        return prefs.getString("provider", null)
    }

    /**
     * This function signout of user and return to login activity
     * @return intentLogin
     */
    fun singOut(): Intent {
        FirebaseAuth.getInstance().signOut()
        val context = getApplicationContext()
        val prefs = context.getSharedPreferences(
            context.getResources().getString(R.string.pref_file),
            Context.MODE_PRIVATE
        ).edit()
        prefs.clear()
        prefs.apply()
        val intentLogin = Intent(context, LoginActivity::class.java)
        return intentLogin
    }

    /**
     * This function save data of user
     * @param typeAuthentication
     * @return Boolean
     */
    fun writeNewUser(typeAuthentication: Authentication): Boolean {
        return if (this.name != "" && this.email != "" && validateEmail(this.email)) {
            val id = FirebaseAuth.getInstance().currentUser?.uid
            val person = checkTypeAuthentication(typeAuthentication)
            val ref = FirebaseDatabase.getInstance().reference
            ref.child("users").child(id.toString()).setValue(person)
            true
        } else {
            false
        }
    }

    /**
     * This function check what type authentication  is used by user
     * @param typeAuthentication
     * @return Person
     */
    private fun checkTypeAuthentication(typeAuthentication: Authentication): Person {
        val person = Person()
        person.name = this.name
        person.email = this.email
        person.provider = typeAuthentication.name
        if (typeAuthentication.name == Authentication.BASIC.toString()) {
            person.password = this.password
        } else {
            person.password = ""
        }
        return person
    }

    /**
     * This function validate if email is correct
     * @param email
     * @return Boolean
     */
    fun validateEmail(email: CharSequence): Boolean {
        val EMAIL_ADDRESS_PATTERN: Pattern =
            Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    fun validatePassword(password: String): Boolean {
        return password == ""
    }

    /**
     * This function check if password is equal to confirm password
     * if  two password is equals, the password is encrypted
     * @param password
     * @param confirmPassword
     * @return String
     */
    fun checkPassword(password: String, confirmPassword: String): String {
        return if (password == "" && confirmPassword == "") {
            "0"
        } else if (password == confirmPassword) {
            getHash(password).toString()
        } else {
            "1"
        }
    }

    /**
     * This function check email field, is required
     */
    fun checkEmail(email: String): Boolean {
        return email == ""
    }

    /**
     * password is encrypted
     * @param password
     * @return String
     */
    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    fun getHash(password: String): String? {
        val md: MessageDigest = MessageDigest.getInstance("MD5")
        val hashPassword: ByteArray = md.digest(password.toByteArray())
        return Base64.encodeToString(hashPassword, Base64.NO_WRAP)
    }

}

