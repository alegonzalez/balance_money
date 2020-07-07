package com.ale.balance_money.logic

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ale.balance_money.R
import kotlinx.android.synthetic.main.item_account.view.*

class AccountAdapter(private val context:Context) : RecyclerView.Adapter<AccountAdapter.MyViewHolder>() {

    private var dataList = mutableListOf<Account>()

    /**
     * This function set all account of user and set to dataList
     */
    fun setDataAccount(data:MutableList<Account>){
        dataList = data
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): AccountAdapter.MyViewHolder {
     val view = LayoutInflater.from(context).inflate(R.layout.item_account,parent,false)
        // create a new view
      return MyViewHolder(view)
    }

    /**
     * Check if datalist with all account of user greater than zero
     */
    override fun getItemCount(): Int {
        return if(dataList.size > 0){
            dataList.size
        }else{
            0
        }
    }

    /**
     * This function get data of datalist by position nd set to holder, then show to recycler view
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val account = dataList[position]
        holder.bindView(account)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        /**
         * This function set data of account and set in view of each elemet
         * For example ImageView to Icon to load
         */
        fun bindView(account:Account){
            when {
                Money.COLON.name == account.money -> {
                    itemView.imgTypeMoney.setImageResource(R.drawable.colon_icon_list)
                    itemView.txtAmountAccount.text = "₡" + account.amount.toString()
                }
                Money.DOLLAR.name == account.money -> {
                    itemView.imgTypeMoney.setImageResource(R.drawable.dollar_icon_list)
                    itemView.txtAmountAccount.text = "$" + account.amount.toString()
                }
                else -> {
                    itemView.imgTypeMoney.setImageResource(R.drawable.euro_icon_list)
                    itemView.txtAmountAccount.text = "€" + account.amount.toString()
                }

            }
            itemView.txtTitle.text = account.title

        }
    }

}