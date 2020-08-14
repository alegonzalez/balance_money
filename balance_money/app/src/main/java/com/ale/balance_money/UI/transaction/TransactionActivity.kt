package com.ale.balance_money.UI.transaction

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ale.balance_money.R
import com.ale.balance_money.UI.category.CategoryActivity
import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.logic.category.Category
import com.ale.balance_money.logic.category.CategoryAdapter
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.logic.transaction.Transaction
import com.ale.balance_money.logic.transaction.TransactionAdapter
import com.ale.balance_money.model.AccountViewModel
import com.ale.balance_money.model.CategoryViewModel
import com.ale.balance_money.model.TransactionViewModel
import com.ale.balance_money.repository.FirebaseData
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_transaction.*
import java.io.Serializable

class TransactionActivity : AppCompatActivity(),TransactionAdapter.OnListenerTransaction  {
    private val viewModelTransaction by lazy { ViewModelProvider(this).get(TransactionViewModel::class.java) }
    private lateinit var recyclerViewTransaction: RecyclerView
    private lateinit var adapterTransaction: TransactionAdapter

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        val btnNewTransaction = findViewById<FloatingActionButton>(R.id.addNewTransaction)
       recyclerViewTransaction = findViewById(R.id.rcyListTranscation)
       if(!Device().isNetworkConnected(this)){
           Device().messageMistakeSnack("Para ver la lista de transacciones, debes estar conectado a internet",recyclerViewTransaction)
       }
      //config recyclerview
       val linearLayoutmanager = LinearLayoutManager(this)
       linearLayoutmanager.orientation = LinearLayoutManager.VERTICAL
       recyclerViewTransaction.layoutManager = linearLayoutmanager
       recyclerViewTransaction.setHasFixedSize(true)
       recyclerViewTransaction.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
       adapterTransaction = TransactionAdapter(this, this)
       recyclerViewTransaction.adapter = adapterTransaction
       //check if categories list is null to call observeAccount and Fill list or set in recyclerview
       if (viewModelTransaction.listTransaction == null) {
           observeAccount()
       } else {
           adapterTransaction.setDataCategory(viewModelTransaction.listTransaction as MutableList<Transaction>)
           adapterTransaction.notifyDataSetChanged()
           shimmer_view_transaction.stopShimmer()
           shimmer_view_transaction.visibility = View.GONE
       }
       //onclick for go to create a new transaction
        btnNewTransaction.setOnClickListener{
             val intentNewTransaction  =  Intent(this, CreateNewTransactionActivity::class.java)
             startActivity(intentNewTransaction)
             Animatoo.animateSlideLeft(this);
        }
    }
    /**
     * this functon is a observable to get all category by user
     * @return void
     */
    private fun observeAccount() {
        shimmer_view_transaction.startShimmer()
        viewModelTransaction.getListTransaction().observe(this, Observer { listTransaction ->
            shimmer_view_transaction.stopShimmer()
            shimmer_view_transaction.visibility = View.GONE
            if (listTransaction.isEmpty()) {
                //this.listEmpty()
            } else {
                viewModelTransaction.listTransaction = listTransaction
                adapterTransaction.setDataCategory(listTransaction)
                adapterTransaction.notifyDataSetChanged()
            }
        })
    }
    /**
     * method when user back the previous activity, I do animation between activities
     */
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this)
        finish()
    }

    override fun OnItemClickTransaction(
        id: String,
        account: String,
        category: String,
        amount: Double,
        typeTransaction: String,
        money:String,
        description: String,
        date:String
    ) {
        val intentDetailTransaction = Intent(this,DetailTransactionActivity::class.java)
        intentDetailTransaction.putExtra("id",id)
        intentDetailTransaction.putExtra("account",account)
        intentDetailTransaction.putExtra("category",category)
        intentDetailTransaction.putExtra("amount",amount)
        intentDetailTransaction.putExtra("money",money)
        intentDetailTransaction.putExtra("typeTransaction",typeTransaction)
        intentDetailTransaction.putExtra("description",description)
        intentDetailTransaction.putExtra("date",date)
        startActivity(intentDetailTransaction)
        Animatoo.animateSlideLeft(this);
    }
}