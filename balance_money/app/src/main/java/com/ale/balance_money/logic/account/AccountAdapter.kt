package com.ale.balance_money.logic.account

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.ale.balance_money.R
import com.ale.balance_money.logic.setting.Device
import kotlinx.android.synthetic.main.item_account.view.*

/**
 * This class is an adapter for account
 * @param context
 * @param onItemClickListener
 * @author Alejandro Alvarado
 */
class AccountAdapter(
  private val context: Context?,
  private val onItemClickListener: OnAccountListener
) : RecyclerView.Adapter<AccountAdapter.AccountHolder>() {

  private var dataList = listOf<AccountMoney>()

  //Interface to implement in this class and also ListAccountTablet  and ListAccountFragment
  interface OnAccountListener {
    fun OnItemClickAccount(
      title: String,
      money: String,
      amount: Double,
      description: String,
      id: String
    )
  }

  /**
   * This function set all account of user and set to dataList
   * @param listAccount
   * return void
   */
  fun setDataAccount(listAccount: List<AccountMoney>) {
    dataList = listAccount
  }

  /**
   * this function check what item load for after show item if a smartphone or tablet
   * @param parent
   * @param viewType
   * @return AccountHolder
   */
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
    val device = Device()
    val orientation = context?.resources?.configuration?.orientation
    val view: View
    val windowManager = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    view = if (!device.detectTypeDevice(windowManager)) {
      if (Device().detectOrientationDevice(orientation)) {
        LayoutInflater.from(context)
          .inflate(R.layout.item_account_tablet_port, parent, false)
      } else {
        LayoutInflater.from(context)
          .inflate(R.layout.item_account_tablet_land, parent, false)
      }
    } else {
      LayoutInflater.from(context).inflate(R.layout.item_account, parent, false)
    }


    // create a new view
    return AccountHolder(view)
  }

  /**
   * this function get size of list
   * @return Int
   */
  override fun getItemCount(): Int {
    return if (dataList.isNotEmpty()) {
      dataList.size
    } else {
      0
    }
  }

  /**
   * this function  call function bindView inside class AccountHolder to set attribute of account by list position
   * @param holder
   * @param position
   * return call function bindView with data of specific position of list
   */
  override fun onBindViewHolder(holder: AccountHolder, position: Int) {
    val account = dataList[position]

    holder.bindView(account)
  }

  // Provide a reference to the views for each data item
  // Complex data items may need more than one view per item, and
  // you provide access to all the views for a data item in a view holder.

  inner class AccountHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * This function set data of account and set in view of each elemet
     * For example ImageView to Icon to load
     * @param account
     */
    fun bindView(account: AccountMoney) {
      itemView.setOnClickListener {
        onItemClickListener.OnItemClickAccount(
          account.title,
          account.money,
          account.amount,
          account.description,
          account.id
        )
      }
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