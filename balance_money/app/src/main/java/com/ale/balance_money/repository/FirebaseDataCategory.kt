package com.ale.balance_money.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ale.balance_money.logic.category.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.ale.balance_money.logic.setting.DatabaseSetting
import java.lang.Exception
/**
 * This class is for any communication with firebase for example an insert, update or delete for category
 * @author Alejandro Alvarado
 */
class FirebaseDataCategory {
    private val uidUser = DatabaseSetting().getUidUser()
    private  val ref = DatabaseSetting().getDatabaseReference()

    /**
     * this function insert new Category in firebase
     * @param category
     * @return void
     */
    fun insertNewCategory(category:Category) {
        ref.child("category").child(uidUser.toString()).push().setValue(category)
    }

    /**
     * This function get all categories by user
     * @return LiveData<List<Category>>
     */
    fun getAllCategories(): LiveData<List<Category>> {
        val mutableData = MutableLiveData<List<Category>>()
        val listCategory = mutableListOf<Category>()
        ref.child("category").child(uidUser.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listCategory.clear()
                for (data in dataSnapshot.children) {
                    val categoryData = Category()
                    categoryData.id = data.key.toString()
                    categoryData.name  = data.child("name").value.toString()
                    categoryData.description = data.child("description").value.toString()
                    listCategory.add(categoryData)
                }
                mutableData.value = listCategory
            }
            override fun onCancelled(databaseError: DatabaseError) {
                //Log.w(TAG, "getUser:onCancelled", databaseError.toException())
                // ...
            }

        })
        return mutableData
    }
    /**
     * This function remove a category from firebase
     * @param id
     * @return Boolean
     */
    fun removeCategory(id:String):Boolean{
        return try {
            ref.child("category").child(uidUser.toString()).child(id).removeValue();
            true
        } catch (e: Exception) {
            false
        }
    }
    /**
     * this function update detail of account
     * @param category
     * @return Boolean
     */
    fun updateCategory( category: Category): Boolean {
        return try {
            ref.child("category").child(uidUser.toString()).child(category.id).setValue(category);
            true
        } catch (e: Exception) {
            false
        }
    }
}