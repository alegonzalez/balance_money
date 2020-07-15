package com.ale.balance_money.logic.Category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.ale.balance_money.R
import com.ale.balance_money.logic.Category.CategoryAdapter.CategoryHolder
import com.ale.balance_money.logic.Device
import com.ale.balance_money.logic.account.Account
import com.ale.balance_money.logic.account.AccountAdapter
import com.ale.balance_money.logic.account.Money
import kotlinx.android.synthetic.main.item_account.view.*
import kotlinx.android.synthetic.main.item_category.view.*

/**
 * In this class I implement an adapter for category
 * @param context
 * @param onItemClickListener
 * @author Alejandro Alvarado
 */
class CategoryAdapter(
    private val context: Context?,
    private val onItemClickListener: OnListenerCategory
) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    private var listCategory = listOf<Category>()

    /**
     * This function set all account of user and set to dataList
     * @param listCategories
     * return void
     */
    fun setDataCategory(listCategories: List<Category>) {
        listCategory = listCategories
    }

    /**
     * interface with fun OnItemClickCategory for implement in class CategoryHolder and CategoryActivity
     */
    interface OnListenerCategory {
        fun OnItemClickCategory(id: String, name: String, description: String)
    }

    /**
     * this function get size of list
     * return Int
     */
    override fun getItemCount(): Int {
        return if (listCategory.isNotEmpty()) {
            listCategory.size
        } else {
            0
        }
    }

    /**
     * this function check what item load for after show item if a smartphone or tablet
     * @param parent
     * @param viewType
     * @return CategoryHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view: View
        val windowManager = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        view = if(Device().detectTypeDevice(windowManager)){
            LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        }else{
            LayoutInflater.from(context).inflate(R.layout.item_category_tablet, parent, false)
        }
        return CategoryHolder(view)
    }

    /**
     * this function  call function bindView inside class CategoryHolder to set name of category by list position
     * @param holder
     * @param position
     * return call function bindView with data of specific position of list
     */
    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        return holder.bindView(listCategory[position])
    }


    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * This function set data of category and set in view of each element
         * @param category
         * @return void
         */
        fun bindView(category: Category) {
            itemView.setOnClickListener {
                onItemClickListener.OnItemClickCategory(
                    category.id,
                    category.name,
                    category.description
                )
            }
            itemView.txtNameCategory.text = category.name
        }
    }
}