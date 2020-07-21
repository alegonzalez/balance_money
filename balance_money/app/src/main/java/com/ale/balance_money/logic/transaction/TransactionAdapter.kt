package com.ale.balance_money.logic.transaction

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_transaction.view.*
import java.util.*

class TransactionAdapter(
    private val context: Context?,
    private val onItemClickListener: TransactionAdapter.OnListenerTransaction
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
            description: String
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class TransactionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * This function set data of category and set in view of each element
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
                    transaction.description
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