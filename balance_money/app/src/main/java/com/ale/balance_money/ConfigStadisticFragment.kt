package com.ale.balance_money

import android.content.Context
import android.os.Bundle
import android.text.InputType.TYPE_NULL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ale.balance_money.logic.setting.Device
import com.ale.balance_money.logic.statistics.Stadistics
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import java.util.*

/**
 * This class is a fragment for show two field for selected dates start and end
 * @author Alejandro Alvarado
 */
class ConfigStadisticFragment : Fragment() {

    private var typeDevice: Boolean = false
    private var orientation = false

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
        val startDate = view.findViewById<EditText>(R.id.editTextDateStart)
        val endDate = view.findViewById<EditText>(R.id.editTextDateEnd)
        val btnCreateGraphic = view.findViewById<Button>(R.id.btnCreateGrafic)
        startDate.inputType = TYPE_NULL;
        endDate.inputType = TYPE_NULL;

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


        //generate graphics
        btnCreateGraphic.setOnClickListener {
            if (Device().isNetworkConnected(activity?.applicationContext!!)) {
                //validate if two field are correct if them field don't correct show message of error
                if (Stadistics().validateDates(startDate, endDate)) {
                    val graficFragment = GraphicFragment()
                    val bundle = Bundle()
                    bundle.putString("startDate", startDate.text.toString())
                    bundle.putString("endDate", endDate.text.toString())
                    graficFragment.arguments = bundle
                    val ft = activity?.supportFragmentManager?.beginTransaction()
                    ft?.setCustomAnimations(
                        R.anim.slide_in_left, R.anim.slide_out_left,
                        R.anim.slide_out_right, R.anim.slide_in_right
                    )
                    if (!typeDevice) {
                        ft?.replace(R.id.containerGrapficFragment, graficFragment)
                        ft?.addToBackStack(null)
                        ft?.commit()
                    } else {

                        ft?.replace(R.id.containerFragment, graficFragment)
                        ft?.addToBackStack(null)
                        ft?.commit()
                    }
                }
            } else {
                Device().messageMistakeSnack(
                    "Para visualizar el gráfico con los resultados, debes estar conectado a internet",
                    startDate
                )
            }

        }
    }
}

