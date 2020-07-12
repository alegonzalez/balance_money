package com.ale.balance_money

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ale.balance_money.UI.account.AccountUpdateDeleteActivity
import com.ale.balance_money.UI.account.NewAccountActivity
import com.ale.balance_money.logic.AccountAdapter
import com.ale.balance_money.model.AccountViewModel
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.list_account_fragment.*


class ListAccountTablet : Fragment(), AccountAdapter.OnAccountListener {
    private val viewModel by lazy { ViewModelProvider(this).get(AccountViewModel::class.java) }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AccountAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_account_tablet, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView =  view.findViewById(R.id.rcyAccount)
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter = AccountAdapter(context,this)
        recyclerView.adapter = adapter
        observeAccount()
        /**
         * Button Floating to create new account, this button is executed when user make click over there
         */
        view.findViewById<FloatingActionButton>(R.id.btnCreateNewAccountTablet).setOnClickListener {
            val intentNewAccount = Intent(context, NewAccountActivity::class.java)
            startActivity(intentNewAccount)
            Animatoo.animateSlideLeft(context);
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
    override fun OnItemClick(title: String, money: String, amount: Double, description: String,id:String) {
        val intentUpdateDelete:Intent = Intent(context, AccountUpdateDeleteActivity::class.java)
        intentUpdateDelete.putExtra("id",id)
        intentUpdateDelete.putExtra("title",title)
        intentUpdateDelete.putExtra("money",money)
        intentUpdateDelete.putExtra("amount",amount)
        intentUpdateDelete.putExtra("description",description)
        startActivity(intentUpdateDelete)
        Animatoo.animateSlideLeft(context)
    }
}