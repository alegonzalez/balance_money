package com.ale.balance_money.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.logic.setting.DatabaseSetting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseUser {
    /**
     * This function get all personal account of user
     * @return LiveData<List<Person>> mutableData
     */
    fun getAccountsOfUser(): LiveData<List<Person>> {
        val reference = DatabaseSetting().getDatabaseReference()
        val mutableData = MutableLiveData<List<Person>>()
        val listAccountUser = ArrayList<Person>()
        reference.child("users").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val accountsUser = Person()
                    accountsUser.name = data.child("name").value.toString()
                    accountsUser.email = data.child("email").value.toString()
                    accountsUser.provider = data.child("provider").value.toString()
                    listAccountUser.add(accountsUser)
                }
                mutableData.value = listAccountUser
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Log.w(TAG, "getUser:onCancelled", databaseError.toException())
                // ...
            }
        })
        return mutableData
    }

    /**
     * This function get account of user
     * @return ArrayList<Person> listAccountUser
     */
    fun getAccountOfUser(): LiveData<List<Person>> {
        val reference = DatabaseSetting().getDatabaseReference()
        val uidUser = FirebaseAuth.getInstance().uid//DatabaseSetting().getUidUser()
        val mutableData = MutableLiveData<List<Person>>()
        val listAccountUser = ArrayList<Person>()
        reference.child("users").child(uidUser.toString()).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val accountsUser = Person()
                    if (data.child("email").value == null) {
                        accountsUser.name = dataSnapshot.child("name").value.toString()
                        accountsUser.email = dataSnapshot.child("email").value.toString()
                        accountsUser.password = dataSnapshot.child("password").value.toString()
                        accountsUser.provider = dataSnapshot.child("provider").value.toString()
                    } else {
                        accountsUser.name = data.child("name").value.toString()
                        accountsUser.email = data.child("email").value.toString()
                        accountsUser.password = data.child("password").value.toString()
                        accountsUser.provider = data.child("provider").value.toString()
                    }

                    listAccountUser.add(accountsUser)
                    mutableData.value = listAccountUser
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //Log.w(TAG, "getUser:onCancelled", databaseError.toException())
                // ...
            }
        })
        return mutableData
    }

    /**
     * this function update information personal of user
     * @param accountUser
     * @return Boolean
     */
    fun updateAccountUser(accountUser: Person): Boolean {
        val reference = DatabaseSetting().getDatabaseReference()
        val uidUser = DatabaseSetting().getUidUser()
        return try {
            reference.child("users").child(uidUser.toString()).setValue(accountUser);
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * this function update information personal of user
     * @param accountUser
     * @return Boolean
     */
    fun updatePasswordUser(accountUser: Person): Boolean {
        val reference = DatabaseSetting().getDatabaseReference()
        val uidUser = DatabaseSetting().getUidUser()
        return try {
            reference.child("users").child(uidUser.toString()).setValue(accountUser);
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * This function save data of user
     * @param typeAuthentication
     * @return Boolean
     */
    fun writeNewUser(typeAuthentication: Authentication, person: Person): Boolean {
        return if (person.name != "" && person.email != "" && person.validateEmail(person.email)) {
            val id = FirebaseAuth.getInstance().currentUser?.uid
            val person = person.checkTypeAuthentication(typeAuthentication)
            val ref = FirebaseDatabase.getInstance().reference
            ref.child("users").child(id.toString()).setValue(person)
            true
        } else {
            false
        }
    }
}