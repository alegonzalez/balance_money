package com.ale.balance_money.UI.transaction

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.ale.balance_money.R
import com.ale.balance_money.UI.category.CategoryActivity
import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.logic.category.Category
import com.ale.balance_money.logic.category.CategoryAdapter
import com.ale.balance_money.logic.transaction.TransactionAdapter
import com.ale.balance_money.model.AccountViewModel
import com.ale.balance_money.model.CategoryViewModel
import com.ale.balance_money.model.TransactionViewModel
import com.ale.balance_money.repository.FirebaseData
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.Serializable

class TransactionActivity : AppCompatActivity(),Serializable {
    private val viewModelTransaction by lazy { ViewModelProvider(this).get(TransactionViewModel::class.java) }
    private lateinit var recyclerViewTransaction: RecyclerView
    private lateinit var adapterTransaction: TransactionAdapter

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        val btnNewTransaction = findViewById<FloatingActionButton>(R.id.addNewTransaction)
       //onclick for go to create a new transaction
        btnNewTransaction.setOnClickListener{
             val intentNewTransaction  =  Intent(this, CreateNewTransactionActivity::class.java)
             startActivity(intentNewTransaction)
             Animatoo.animateSlideLeft(this);
        }
    }
    /**
     * method when user back the previous activity, I do animation between activities
     */
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this)
        finish()
    }
}