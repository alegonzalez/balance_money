package com.ale.balance_money.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ale.balance_money.logic.Category.Category
import com.ale.balance_money.repository.FirebaseDataCategory

/**
 * This class is for implement Architecture ViewModel for category
 * @author Alejandro Alvarado
 */
class CategoryViewModel:ViewModel() {

    var listCategories: List<Category>? = null

    /**
     * This function get categories list
     * @return LiveData<List<Category>>
     */
    fun getListCategories(): LiveData<List<Category>> {
        return FirebaseDataCategory().getAllCategories()
    }
}