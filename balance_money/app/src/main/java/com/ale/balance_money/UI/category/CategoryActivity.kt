package com.ale.balance_money.UI.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ale.balance_money.R
import com.ale.balance_money.logic.category.Category
import com.ale.balance_money.logic.category.CategoryAdapter
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.model.CategoryViewModel
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity(), CategoryAdapter.OnListenerCategory {
    private val viewModel by lazy { ViewModelProvider(this).get(CategoryViewModel::class.java) }
    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var adapterCategory: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        recyclerViewCategory = findViewById(R.id.rcyCategory)
        val btnAddNewCategory = findViewById<FloatingActionButton>(R.id.addNewCategory)
        if(!Device().isNetworkConnected(this)){
            Device().messageMistakeSnack("Para ver la lista de categorías, debes estar conectado a internet",recyclerViewCategory)
        }
        val linerLayoutmanager = LinearLayoutManager(this)
        linerLayoutmanager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewCategory.layoutManager = linerLayoutmanager
        recyclerViewCategory.setHasFixedSize(true)
        recyclerViewCategory.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        adapterCategory = CategoryAdapter(this, this)
        recyclerViewCategory.adapter = adapterCategory
       //check if categories list is null to call observeAccount and Fill list or set in recyclerview
        if (viewModel.listCategories == null) {
            observeAccount()
        } else {
            adapterCategory.setDataCategory(viewModel.listCategories as MutableList<Category>)
            adapterCategory.notifyDataSetChanged()
            shimmer_view_category.stopShimmer()
            shimmer_view_category.visibility = View.GONE
        }
        //onclick to go activity and you can add  new category
        btnAddNewCategory.setOnClickListener {
            val intentNewCategory = Intent(this, CreateNewCategoryActivity::class.java)
            startActivity(intentNewCategory)
            Animatoo.animateSlideLeft(this);
        }
    }

    /**
     * this functon is a observable to get all category by user
     * @return void
     */
    private fun observeAccount() {
        shimmer_view_category.startShimmer()
        viewModel.getListCategories()?.observe(this, Observer { listCategories ->
            shimmer_view_category.stopShimmer()
            shimmer_view_category.visibility = View.GONE
            if (listCategories.isEmpty()) {
                this.listEmpty()
            } else {
                viewModel.listCategories = listCategories
                adapterCategory.setDataCategory(listCategories)
                adapterCategory.notifyDataSetChanged()
            }

        })
    }

    /**
     *Show message if does't have categories
     * @return void
     */
    private fun listEmpty() {
        Device()
            .messageSuccessfulSnack("No hay categorías registradas", recyclerViewCategory)
    }

    /**
     * This function is execute when user make click over item and open detail category activity with data
     * @param id
     * @param name
     * @param description
     */
    override fun OnItemClickCategory(id: String, name: String, description: String) {
        val intentCategoryUpdateDelete = Intent(this, UpdateDeleteCategoryActivity::class.java)
        intentCategoryUpdateDelete.putExtra("id", id)
        intentCategoryUpdateDelete.putExtra("name", name)
        intentCategoryUpdateDelete.putExtra("description", description)
        startActivity(intentCategoryUpdateDelete)
        Animatoo.animateSlideLeft(this);
    }

    //This function make animation when user back activity
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this);
    }
}