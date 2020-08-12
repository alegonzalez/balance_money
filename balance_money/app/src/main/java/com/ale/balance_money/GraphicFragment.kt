package com.ale.balance_money


import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ale.balance_money.logic.account.AccountMoney
import com.ale.balance_money.logic.account.Money
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.logic.statistics.Stadistics
import com.ale.balance_money.logic.transaction.Transaction
import com.ale.balance_money.model.TransactionViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.*
import kotlin.collections.ArrayList

/**
 * This class is a fragment for show grafic with the results
 * @author Alejandro Alvarado
 */
class GraphicFragment : Fragment() {

    private lateinit var progressDialog: ProgressDialog
    private val viewModelTransaction by lazy { ViewModelProvider(this).get(TransactionViewModel::class.java) }
    private var listTransaction = listOf<Transaction>()
    private  var typeDevice:Boolean = false
    private var orientation = false
    /**
     * Load fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val device = Device()
       this.orientation = Device().detectOrientationDevice(activity?.applicationContext?.resources?.configuration?.orientation)
       this.typeDevice = device.detectTypeDevice(context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        return if(typeDevice){
            if(this.orientation){
                inflater.inflate(R.layout.fragment_graphic_port, container, false)
            }else{
                inflater.inflate(R.layout.fragment_graphic_land, container, false)
            }
        }else{
            if(this.orientation){
                inflater.inflate(R.layout.fragment_graphic_tablet_port, container, false)
            }else{
                inflater.inflate(R.layout.fragment_graphic_tablet_land, container, false)
            }
        }
    }
    /**
     * This function load init element UI and set event
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressDialog = ProgressDialog(context)
        progressDialog.show()
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.setCanceledOnTouchOutside(false);
         super.onViewCreated(view, savedInstanceState)
        val piechart: PieChart = view.findViewById(R.id.stadisticGraphic)
        this.configPieChart(piechart)
        val accountMoney = AccountMoney()

        val btnColon = view.findViewById<Button>(R.id.btnColonGrafic)
        val btnDollar = view.findViewById<Button>(R.id.btnDollarGrafic)
        val btnEuro = view.findViewById<Button>(R.id.btnEuroGrafic)
        var startDate: Date = Date()
        var endDate: Date = Date()
        accountMoney.setMoney(Money.COLON.name,this.typeDevice,btnColon,btnDollar,btnEuro)
        if (arguments?.getString("startDate") != null && arguments?.getString("endDate") != null) {
            val statistics = Stadistics()
            var tempSplitDate = (arguments?.getString("startDate"))?.split("/")
            if (tempSplitDate != null) {
                startDate = statistics.convertStringToDate(
                    tempSplitDate.get(2),
                    tempSplitDate.get(1),
                    tempSplitDate.get(0)
                )
            }
            tempSplitDate = (arguments?.getString("endDate"))?.split("/")
            if (tempSplitDate != null) {
                endDate = statistics.convertStringToDate(
                    tempSplitDate.get(2),
                    tempSplitDate.get(1),
                    tempSplitDate.get(0)
                )
            }
        }
        if (viewModelTransaction.listTransaction == null) {
            observeTransactions(piechart, startDate, endDate)
        } else {
            this.listTransaction = viewModelTransaction.listTransaction!!
            this.createGraphic(
                piechart,
                startDate,
                endDate,
                "Ingresos",
                "Gastos",
                Money.COLON.name
            )
        }
        //Onclick for show result in grafic with colon money
        btnColon.setOnClickListener {
            accountMoney.setMoney(Money.COLON.name,this.typeDevice,btnColon,btnDollar,btnEuro)
            this.createGraphic(
                piechart,
                startDate,
                endDate,
                "Ingresos",
                "Gastos",
                Money.COLON.name
            )
        }
        //Onclick for show result in grafic with dollar money
        btnDollar.setOnClickListener {
            accountMoney.setMoney(Money.DOLLAR.name,this.typeDevice,btnColon,btnDollar,btnEuro)
            this.createGraphic(
                piechart,
                startDate,
                endDate,
                "Ingresos",
                "Gastos",
                Money.DOLLAR.name
            )
        }
        //Onclick for show result in grafic with euro money
        btnEuro.setOnClickListener {
            accountMoney.setMoney(Money.EURO.name,this.typeDevice,btnColon,btnDollar,btnEuro)

            this.createGraphic(
                piechart,
                startDate,
                endDate,
                "Ingresos",
                "Gastos",
                Money.EURO.name
            )
        }
    }

    /**
     * this functon is a observable to get all account by user
     * @param dropdownAccount
     * @param dropdownCategory
     */
    private fun observeTransactions(pieChart: PieChart, startDate: Date, endDate: Date) {
        viewModelTransaction.getListTransaction()
            .observe(viewLifecycleOwner, Observer { listTransactions ->
                viewModelTransaction.listTransaction = listTransactions
                this.listTransaction = listTransactions
                this.createGraphic(
                    pieChart,
                    startDate,
                    endDate,
                    "Ingreso",
                    "Gasto",
                    Money.COLON.name
                )
            })
    }

    /**
     * This function create stadistic for user
     * @param listTransactions
     * @return Void
     */
    private fun createGraphic(
        pieChart: PieChart,
        startDate: Date,
        endDate: Date,
        labelIncome: String,
        labelExpense: String,
        typeMoney: String
    ) {
        val list = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        val stadistic = Stadistics()
        val listIncomeExpense = stadistic.getAllAmountbyMoney(
            this.listTransaction,
            typeMoney,
            startDate,
            endDate
        )
        if (listIncomeExpense.size > 0) {
            if (listIncomeExpense.get(0).toDouble() > 0) {
                list.add(PieEntry(listIncomeExpense.get(0).toFloat(), labelIncome))
                listColors.add(resources.getColor(R.color.colorButton))
            }
            if (listIncomeExpense.get(1).toDouble() > 0) {
                list.add(PieEntry(listIncomeExpense.get(1).toFloat(), labelExpense))
                listColors.add(resources.getColor(R.color.btnremove))
            }
            val pieDataSet = PieDataSet(list, "")
            pieDataSet.colors = listColors
            pieDataSet.valueTextColor = Color.WHITE
            if(typeDevice){ pieDataSet.valueTextSize = 19F }else{ pieDataSet.valueTextSize = 35F }
            pieChart.centerText = ""
            pieDataSet.sliceSpace = 3F

            val pieData = PieData(pieDataSet)
            pieChart.invalidate()
            pieChart.data = pieData
            pieChart.animateY(1000, Easing.EaseInOutCubic)
            if (listIncomeExpense.get(0).toDouble() == 0.0 && listIncomeExpense.get(1)
                    .toDouble() == 0.0
            ) {
                pieChart.centerText = "No hay registro"
                pieChart.setCenterTextColor(Color.WHITE)
                pieChart.setNoDataText("No hay transacciones disponibles");
            }
            progressDialog.dismiss()
        }


    }

    /**
     * Config of pie chart
     * @param pieChart
     * @return void
     */
    private fun configPieChart(pieChart: PieChart) {
        pieChart.description.isEnabled = false
        val legend = pieChart.legend
        if(this.typeDevice) {
            pieChart.setEntryLabelTextSize(19F)
            pieChart.setCenterTextSize(19F)
            legend.textSize = 19F
            legend.formSize = 19F
        } else{
            pieChart.setEntryLabelTextSize(35F)
            pieChart.setCenterTextSize(35F)
            legend.textSize = 35F
            legend.formSize = 35F
        }

        pieChart.setDrawSliceText(false)
        pieChart.setExtraOffsets(5F, 10F, 5F, 5F)
        pieChart.dragDecelerationFrictionCoef = 0.99F
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleRadius(0F);
        pieChart.setEntryLabelColor(Color.WHITE)
        legend.textColor = Color.WHITE
      if(!orientation){
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT;
        }
    }
}
