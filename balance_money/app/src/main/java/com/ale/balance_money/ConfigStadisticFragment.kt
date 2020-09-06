package com.ale.balance_money

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType.TYPE_NULL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.logic.statistics.Stadistics
import com.ale.balance_money.logic.transaction.Transaction
import com.ale.balance_money.model.AccountViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import java.util.*

/**
 * This class is a fragment for show two field for selected dates start and end
 * @author Alejandro Alvarado
 */
class ConfigStadisticFragment : Fragment() {
    private val viewModelAccount by lazy { ViewModelProvider(this).get(AccountViewModel::class.java) }
    private var listAccount = listOf<String>()
    private var typeDevice: Boolean = false
    private var orientation = false
    private val device = Device()
    private lateinit var progressDialog: ProgressDialog

    /**
     * Load fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get type device
        this.typeDevice =
            Device().detectTypeDevice(context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        //get orientation device
        this.orientation =
            Device().detectOrientationDevice(this.resources.configuration.orientation)
        //check type device
        if (typeDevice) {
            //check orientation for smartphone
            if (orientation) {
                return inflater.inflate(R.layout.fragment_config_stadistic_port, container, false)
            } else {
                return inflater.inflate(R.layout.fragment_config_stadistic_land, container, false)
            }
        } else {
            //check orientation for tablet
            if (orientation) {
                return inflater.inflate(
                    R.layout.fragment_config_stadistic_tablet_port,
                    container,
                    false
                )
            } else {
                return inflater.inflate(
                    R.layout.fragment_config_stadistic_tablet_land,
                    container,
                    false
                )
            }
        }
    }

    /**
     * This function load init element UI and set event
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        val startDate = view.findViewById<EditText>(R.id.editTextDateStart)
        val endDate = view.findViewById<EditText>(R.id.editTextDateEnd)
        val btnCreateGraphic = view.findViewById<Button>(R.id.btnCreateGrafic)
        val dropdownAccount = view.findViewById<Spinner>(R.id.dropdownAccount)
        var nameAccount = ""
        startDate.inputType = TYPE_NULL;
        endDate.inputType = TYPE_NULL;
        this.startDialog()
        if (viewModelAccount.listAccount == null) {
            this.observeAccount(dropdownAccount)
        } else {
            this.listAccount = Transaction().fillListAccount(viewModelAccount.listAccount)
            this.fillDropdownAccount(dropdownAccount)
        }
        val calendar: Calendar = Calendar.getInstance()
        calendar.clear()
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setSelection(today)
        val materialDatePickerStartDate = builder.build()
        val materialDatePickerEndDate = builder.build()
        //onclick show calendar for select start date
        startDate.setOnClickListener {
            if (!materialDatePickerStartDate.isVisible) {
                materialDatePickerStartDate.show(activity?.supportFragmentManager!!, "DATE_PICKER")
            }
        }
        //onclick show calendar for select end date
        endDate.setOnClickListener {
            if (!materialDatePickerEndDate.isVisible) {
                materialDatePickerEndDate.show(activity?.supportFragmentManager!!, "DATE_PICKER")
            }
        }
        materialDatePickerStartDate.addOnPositiveButtonClickListener(
            MaterialPickerOnPositiveButtonClickListener<Long?> {
                startDate.setText(Stadistics().getconvertDate(it!!))
            })
        materialDatePickerEndDate.addOnPositiveButtonClickListener(
            MaterialPickerOnPositiveButtonClickListener<Long?> {
                endDate.setText(Stadistics().getconvertDate(it!!))
            })
        //event when user has  selected  an account
        dropdownAccount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                nameAccount = dropdownAccount.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        //generate graphics
        btnCreateGraphic.setOnClickListener {
            if (Device().isNetworkConnected(activity?.applicationContext!!)) {
                if (listAccount.isEmpty()) {
                    this.device.messageMistakeSnack(
                        "Debes crear una cuenta para observar el resultado",
                        startDate
                    )
                } else {
                    //validate if two field are correct if them field don't correct show message of error
                    if (Stadistics().validateDates(startDate, endDate)) {
                        val graficFragment = GraphicFragment()
                        val bundle = Bundle()
                        bundle.putString("startDate", startDate.text.toString())
                        bundle.putString("endDate", endDate.text.toString())
                        bundle.putString("account", nameAccount)
                        graficFragment.arguments = bundle
                        val ft = activity?.supportFragmentManager?.beginTransaction()
                        ft?.setCustomAnimations(
                            R.anim.slide_in_left, R.anim.slide_out_left,
                            R.anim.slide_out_right, R.anim.slide_in_right
                        )
                        if (!typeDevice) {
                            ft?.replace(R.id.containerGrapficFragment, graficFragment)
                            ft?.commit()
                        } else {
                            ft?.replace(R.id.containerFragment, graficFragment)
                            ft?.addToBackStack(null)
                            ft?.commit()
                        }
                    }
                }

            } else {
                this.device.messageMistakeSnack(
                    "Para visualizar el gráfico con los resultados, debes estar conectado a internet",
                    startDate
                )
            }

        }
    }

    /**
     * This function fill dropdown account
     * @param dropdownAccount
     * return void
     */
    private fun fillDropdownAccount(dropdownAccount: Spinner) {
        val messageAccount = arrayOf("No hay cuentas")
        setElementInSpinner(listAccount, messageAccount, dropdownAccount)
    }

    /**
     * this functon is a observable to get all account by user
     * @param dropdownAccount
     * @param dropdownCategory
     */
    private fun observeAccount(dropdownAccount: Spinner) {
        viewModelAccount.fetchAccount()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { listAccount ->
                viewModelAccount.listAccount = listAccount
                this.listAccount = Transaction().fillListAccount(listAccount)
                fillDropdownAccount(dropdownAccount)
            })
    }

    /**
     * This function set data in spinner when device is smartphone or tablet
     * @param list
     * @param emptyList
     * @param dropdown
     */
    private fun setElementInSpinner(
        list: List<String>,
        emptyList: Array<String>,
        dropdown: Spinner
    ) {
        val orientation =
            Device().detectTypeDevice(requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        val adapter: ArrayAdapter<String>
        if (orientation) {
            if (list.isEmpty()) {
                adapter =
                    ArrayAdapter(requireContext(), R.layout.spinner_dropdown_layout, emptyList)
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            } else {
                adapter = ArrayAdapter(requireContext(), R.layout.spinner_dropdown_layout, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner);
            }
            dropdown.adapter = adapter
        } else {
            if (list.isEmpty()) {
                adapter =
                    ArrayAdapter(requireContext(), R.layout.spinner_dropdown_tablet, emptyList)
                adapter.setDropDownViewResource(R.layout.custom_spinner_tablet);
            } else {
                adapter = ArrayAdapter(requireContext(), R.layout.spinner_dropdown_tablet, list)
                adapter.setDropDownViewResource(R.layout.custom_spinner_tablet);
            }
            dropdown.adapter = adapter
        }
        if (viewModelAccount.listAccount != null) {
            progressDialog.dismiss()
        }
    }

    /**
     * This function start dialog waiting
     */
    private fun startDialog() {
        progressDialog.setMessage("Espere un momento por favor...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }
}





