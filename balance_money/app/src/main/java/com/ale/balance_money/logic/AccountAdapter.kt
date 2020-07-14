package com.ale.balance_money.logic

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.ale.balance_money.R
import kotlinx.android.synthetic.main.item_account.view.*


class AccountAdapter(private val context: Context?, private val onItemClickListener:OnAccountListener) : RecyclerView.Adapter<AccountAdapter.AccountHolder>(){

    private var dataList = listOf<Account>()

    //Interface to implement in this class and also AccountActivity
    interface  OnAccountListener{
        fun OnItemClick(title: String,money:String,amount:Double,description:String,id:String)
    }

    /**
     * This function set all account of user and set to dataList
     */
    fun setDataAccount(data:List<Account>){
        dataList = data
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): AccountHolder {
        val device = Device()
        val orientation= context?.resources?.configuration?.orientation
        val view:View
        val windowManager = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if(!device.detectTypeDevice(windowManager)){
            if(Device().detectOrientationDevice(orientation)){
                view = LayoutInflater.from(context).inflate(R.layout.item_account_tablet_port,parent,false)
            } else{
                view = LayoutInflater.from(context).inflate(R.layout.item_account_tablet_land,parent,false)
            }
        }else{
             view = LayoutInflater.from(context).inflate(R.layout.item_account,parent,false)
        }



        // create a new view
      return AccountHolder(view)
    }

    /**
     * Check if datalist with all account of user greater than zero
     */
    override fun getItemCount(): Int {
        return if(dataList.isNotEmpty()){
            dataList.size
        }else{
            0
        }
    }

    /**
     * This function get data of datalist by position nd set to holder, then show to recycler view
     */
    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        val account = dataList[position]

        holder.bindView(account)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.

    inner class AccountHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        /**
         * This function set data of account and set in view of each elemet
         * For example ImageView to Icon to load
         */
        fun bindView(account:Account){
            itemView.setOnClickListener{onItemClickListener.OnItemClick(account.title,account.money,account.amount,account.description,account.id)}
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