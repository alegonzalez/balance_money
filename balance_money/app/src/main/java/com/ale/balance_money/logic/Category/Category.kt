package com.ale.balance_money.logic.Category

/**
 * this class is for logic to category
 * @author Alejandro Alvarado
 */
class Category {
    var id: String = ""
    var name: String = ""
    var description: String = ""

    /**
     * This function validate the field name and call function insertNewCategory() in FirebaseDataCategory
     * @return Boolean
     */
    fun validateField(): Boolean {
        return this.name != ""
    }


}