package com.ale.balance_money


import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import java.util.regex.Pattern


enum class Authentication{
    FACEBOOK,TWITER
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
       //if(!isLoggedIn()){
        checkIfExistUser()
           var id = createTransactionID().toString()
           if(this.name != "" && this.email != "" && validateEmail(this.email)){
               val person = checkTypeAutentication(typeAuthentication)
               val ref = FirebaseDatabase.getInstance().reference
               ref.child("balance_money").child("users").setValue(person)
               return true
           }else{
               return false
           }
       }



    fun checkIfExistUser():Boolean{


        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val rootRef = FirebaseDatabase.getInstance().reference
        val uidRef = rootRef.child("balance_money").child("users").child(this.email)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.i("Message","Create a new user")
                }else{
                    Log.i("Message","User exist")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        uidRef.addListenerForSingleValueEvent(eventListener)
        return true
    }
    /**
     * This function check what type authenticacion  do you use
     */
    fun checkTypeAutentication(typeAuthentication: Authentication):Person{
        val person = Person()
        if(typeAuthentication.name.equals(Authentication.FACEBOOK)){
            person.name = this.name
            person.email = this.email
        }else if (typeAuthentication.name.equals(Authentication.TWITER)){

        }else{
            person.name = this.name
            person.email = this.email
            person.password = this.password
        }
        return person
    }
    @Throws(Exception::class)
    fun createTransactionID(): String? {
        return UUID.randomUUID().toString().replace('-','_')
    }
    /**
     * This function validate if email is correct
     */
    fun validateEmail(email:CharSequence): Boolean {
        val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


}

