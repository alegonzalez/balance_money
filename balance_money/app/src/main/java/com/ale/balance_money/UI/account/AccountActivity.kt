package com.ale.balance_money.UI.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ale.balance_money.R
import com.ale.balance_money.logic.Account
import com.ale.balance_money.logic.AccountAdapter
import com.ale.balance_money.model.AccountViewModel
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.content_account.*


class AccountActivity : AppCompatActivity(),AccountAdapter.OnAccountListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AccountAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(AccountViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setSupportActionBar(findViewById(R.id.toolbar))
        recyclerView =  findViewById(R.id.rcyAccount)
        adapter = AccountAdapter(this,this)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter

        observeAccount()
        /**
         * Button Floating to create new account, this button is executed when user make click over there
         */
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intentNewAccount = Intent(this,
                NewAccountActivity::class.java)
            startActivity(intentNewAccount)
            Animatoo.animateSlideLeft(this);

        }
        }

    /**
     * this functon is a observable to get all account by user
     */
    private fun observeAccount(){
        shimmer_view_container.startShimmer()
        viewModel.fetchAccount().observe(this, Observer {listAccount->
            shimmer_view_container.stopShimmer()
            shimmer_view_container.visibility = View.GONE
            adapter.setDataAccount(listAccount)
            adapter.notifyDataSetChanged()
        })
    }
  /**
     * method when user back the previous activity, I do animation between activities
   */
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this);
    }


    /**
     * This function is exeuted when user make click
     */
    override fun OnItemClick(title: String,money:String,amount:Double,description:String) {
        Toast.makeText(this, "Click to $amount",Toast.LENGTH_LONG).show()
    }


}