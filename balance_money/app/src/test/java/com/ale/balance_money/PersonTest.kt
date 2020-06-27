package com.ale.balance_money


import junit.framework.Assert.assertTrue
import junit.framework.TestCase.assertFalse
import org.junit.Test

class PersonTest{

    private val person:Person = Person("Alejandro Alvarado","","")
    /*
    test without email should return false
    */
    @Test
    fun emailEmptyValidator() {
            assertFalse("empty email", person.writeNewUser(Authentication.FACEBOOK))
    }
    /*
        test without name should return false
        */
    @Test
    fun nameValidator() {
        person.email = "aalvarado@tabacon.com"
        person.name = "";
        assertFalse("empty name", person.writeNewUser(Authentication.FACEBOOK))
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