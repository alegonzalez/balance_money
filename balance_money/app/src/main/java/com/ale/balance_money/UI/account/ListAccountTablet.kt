package com.ale.balance_money.UI.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ale.balance_money.R
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.logic.account.AccountAdapter
import com.ale.balance_money.model.AccountViewModel
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.list_account_fragment.*

/**
 * This  class is for show fragment for tablet to show accounts list
 * @author Alejandro Alvarado
 */
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
    /**
     * This function load after of onCreateView and show list of accounts
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView =  view.findViewById(R.id.rcyAccount)
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter =
            AccountAdapter(context, this)
        recyclerView.adapter = adapter
       if(viewModel.listAccount == null){
           observeAccount()
       }else{
           adapter.setDataAccount(viewModel.listAccount as MutableList<AccountMoney>)
           adapter.notifyDataSetChanged()
           shimmer_view_container.stopShimmer()
           shimmer_view_container.visibility = View.GONE
       }


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
        viewModel.fetchAccount()?.observe(viewLifecycleOwner, Observer { listAccount->
            shimmer_view_container.stopShimmer()
            shimmer_view_container.visibility = View.GONE
            if(listAccount.isEmpty()){
                this.listEmpty()
            }else{
                viewModel.listAccount = listAccount
                adapter.setDataAccount(listAccount)
                adapter.notifyDataSetChanged()
            }

        })
    }

    /**
     * this function show message if user doesn't account
     */
    private fun listEmpty(){
    Device().messageSuccessfulSnack("No hay cuentas registradas",view)
    }
    /**
     * This function is executed when user make click
     * @param title
     * @param money
     * @param amount
     * @param description
     * @param id
     */
    override fun OnItemClickAccount(title: String, money: String, amount: Double, description: String,id:String) {
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