package com.ale.balance_money


import android.util.Base64
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern


enum class Authentication{
    FACEBOOK,TWITER,BASIC
}
class Person {
     var name:String
     var email:String
     var password:String

     constructor(name: String, email: String,password: String = "") {
        this.name = name
        this.email = email
        this.password = password;
    }
    constructor() {
        this.name = ""
        this.email = ""
        this.password = ""
    }


    /**
     * This function check if user is log in
     */
    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null
    }

    /**
     * This function save data of user
     */
    fun writeNewUser(typeAuthentication:Authentication): Boolean {
        return if(this.name != "" && this.email != "" && validateEmail(this.email)){
            val id = FirebaseAuth.getInstance().currentUser?.uid

            val person = checkTypeAutentication(typeAuthentication)
            val ref = FirebaseDatabase.getInstance().reference
            ref.child("users").child(id.toString()).setValue(person)
            true
        }else{
            false
        }
       }

    /**
     * This function check what type authenticacion  do you use
     */
    private fun checkTypeAutentication(typeAuthentication: Authentication):Person{
        val person = Person()
        person.name = this.name
        person.email = this.email
        if(typeAuthentication.name == Authentication.BASIC.toString()){
           person.password = this.password
        }else{
            person.password = ""
        }
        return person
    }

    /**
     * This function validate if email is correct
     */
    fun validateEmail(email:CharSequence): Boolean {
        val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    /**
     * This function check if password is equal to confirm password
     * if  two password is equals, the password is encrypted
     */
    fun checkPassword(password: String, confirmPassword: String):String {
        if(password == "" && confirmPassword == "") {
           // return "El campo contraseña es requerido"
            return "0"
        }else if(password == confirmPassword){
            return getHash(password).toString()
        }else{
           // return "Los campos contraseña y confirmar contraseña no coinciden"
            return "1"
        }
    }

    /**
     * This function check email field, is required
     */
    fun checkEmail(email:String):Boolean{
        return email == ""
    }
    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
     fun getHash(password: String): String? {
        val md: MessageDigest = MessageDigest.getInstance("MD5")
        val hashPassword: ByteArray = md.digest(password.toByteArray())
        return Base64.encodeToString(hashPassword,Base64.NO_WRAP)
    }

}

