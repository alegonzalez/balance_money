package com.ale.balance_money.logic.transaction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.ale.balance_money.R
import com.ale.balance_money.logic.setting.Device
import kotlinx.android.synthetic.main.item_transaction.view.*

/**
 * This class is an adapter for transactions
 * @author Alejandro Alvarado
 * @param context
 * @param onItemClickListener
 */
class TransactionAdapter(
    private val context: Context?,
    private val onItemClickListener:OnListenerTransaction
) : RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {
    private var listTransaction = listOf<Transaction>()

    /**
     * This function set all account of user and set to dataList
     * @param listCategories
     * return void
     */
    fun setDataCategory(listTransactions: List<Transaction>) {
        listTransaction = listTransactions
    }

    /**
     * interface with fun OnItemClickTransaction for implement in class TransactionHolder and TransactionActivity
     */
    interface OnListenerTransaction {
        fun OnItemClickTransaction(
            id: String,
            account: String,
            category: String,
            amount: Double,
            typeTransaction: String,
            money:String,
            description: String,
            date:String
        )
    }
    /**
     * this function check what item load for after show item if a smartphone or tablet
     * @param parent
     * @param viewType
     * @return TransactionHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val view: View
        val windowManager = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        view = if(Device().detectTypeDevice(windowManager)){
            LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false)
        }else{
            LayoutInflater.from(context).inflate(R.layout.item_transaction_tablet, parent, false)
        }
        return TransactionHolder(view)
    }

    override fun getItemCount(): Int {
        if(listTransaction.isNotEmpty()){
            return listTransaction.size
        }else{
            return 0
        }
    }
    /**
     * this function  call function bindView inside class TransactionHolder to set name of transaction by list position
     * @param holder
     * @param position
     * return call function bindView with data of specific position of list
     */
    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        return holder.bindView(listTransaction[position])
    }

    inner class TransactionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * This function set data of transaction and set in view of each element
         * @param category
         * @return void
         */
        fun bindView(transaction: Transaction) {
            itemView.setOnClickListener {
                onItemClickListener.OnItemClickTransaction(
                    transaction.id,
                    transaction.account,
                    transaction.category,
                    transaction.amount,
                    transaction.typeTransaction,
                    transaction.money,
                    transaction.description,
                    transaction.dateOfTrasaction

                )
            }
            itemView.txtNameAccountTransaction.text = transaction.account
            itemView.txtNameCategoryTransaction.text = transaction.category
            itemView.txtDateTransaction.text = transaction.category
            val dateNow = Transaction().getDateNow()
            itemView.txtDateTransaction.text = dateNow
            transaction.dateOfTrasaction = dateNow
        }
    }


}