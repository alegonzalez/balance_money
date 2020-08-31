package com.ale.balance_money


import com.ale.balance_money.logic.Authentication
import com.ale.balance_money.logic.Person
import com.ale.balance_money.repository.FirebaseUser
import junit.framework.Assert.assertTrue
import junit.framework.TestCase.assertFalse
import org.junit.Test

class PersonTest {

    private val person: Person =
        Person("Alejandro Alvarado", "", "", Authentication.BASIC.name)
    private val firebaseUser = FirebaseUser()

    /*
    test without email should return false
    */
    @Test
    fun emailEmptyValidator() {
        assertFalse("empty email", firebaseUser.writeNewUser(Authentication.FACEBOOK, person))
    }

    /*
        test without name should return false
        */
    @Test
    fun nameValidator() {
        person.email = "aalvarado@tabacon.com"
        person.name = "";
        assertFalse("empty name", firebaseUser.writeNewUser(Authentication.FACEBOOK, person))
    }

    /*
        test email is valid
        */
    @Test
    fun emailValid() {
        person.email = "aalvarado@tabacon.com"
        assertTrue("email is valid", person.validateEmail(person.email))
    }

    /*
            test email is invalid
            */
    @Test
    fun emailInvalid() {
        person.email = "aalvaradotabacon.com"
        assertFalse("Email is invalid", person.validateEmail(person.email))
    }

}