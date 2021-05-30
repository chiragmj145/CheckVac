package com.workfromhome.checkvac.Activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.workfromhome.checkvac.Adapters.CenterAdapter
import com.workfromhome.checkvac.Models.CenterModel
import com.workfromhome.checkvac.R
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var centerList: List<CenterModel>
    lateinit var adapter: CenterAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        centerList = ArrayList<CenterModel>()
        covidCasesButton.setOnClickListener {
            startActivity(Intent(this, CovidCasesActivity::class.java))
        }

        searchButton.setOnClickListener {
            closeKeyboard()
            val pinCode = pinCodeEditText.text.toString()
            if (pinCode.length != 6) {
                Toast.makeText(this, "Please Enter a Valid PIN code", Toast.LENGTH_SHORT).show()
            } else {
                vaccineRecyclerView.visibility = View.GONE
                vaccineNotFoundTextView.visibility = View.GONE
                delivery.visibility = View.GONE
                (centerList as ArrayList<CenterModel>).clear()
                // Date Picker Dialog
                val calender = Calendar.getInstance()
                val year = calender.get(Calendar.YEAR)
                val month = calender.get(Calendar.MONTH)
                val day = calender.get(Calendar.DAY_OF_MONTH)

                val dialog = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        vaccine.visibility = View.VISIBLE
                        enterYourPinTextView.visibility = View.GONE
                        // to get the date selected by the user
                        val selectedDate: String = """$dayOfMonth-${month + 1}-$year"""
                        val SPLASH_TIME_OUT: Long = 2000 // 2 sec

                        Handler().postDelayed(
                            {
                                getAppointmentDetails(pinCode, selectedDate)
                            },
                            SPLASH_TIME_OUT
                        )
                    },
                    year,
                    month,
                    day
                )
                dialog.show()
            }
        }
    }

    private fun getAppointmentDetails(pinCode: String, date: String) {
        val url =
            "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pinCode + "&date=" + date
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                vaccine.visibility = View.GONE
                try {
                    val centerArray = response.getJSONArray("centers")
                    if (centerArray.length().equals(0)) {
                        Toast.makeText(this, "No Vaccination Centers Available", Toast.LENGTH_SHORT)
                            .show()
                        vaccineNotFoundTextView.visibility = View.VISIBLE
                        delivery.visibility = View.VISIBLE
                    }
                    for (i in 0 until centerArray.length()) {
                        val centerObject = centerArray.getJSONObject(i)
                        val centerName: String = centerObject.getString("name")
                        val centerAddress: String = centerObject.getString("address")
                        val centerFromtime: String = centerObject.getString("from")
                        val centerTotime: String = centerObject.getString("to")
                        val fee_type: String = centerObject.getString("fee_type")
                        // getting sessions array and object inside it.
                        val sessionObject = centerObject.getJSONArray("sessions").getJSONObject(0)
                        val availability: Int = sessionObject.getInt("available_capacity")
                        val ageLimit: Int = sessionObject.getInt("min_age_limit")
                        val vaccineName: String = sessionObject.getString("vaccine")

                        // passing the data inside our model list
                        val center = CenterModel(
                            centerName,
                            centerAddress,
                            centerFromtime,
                            centerTotime,
                            fee_type,
                            ageLimit,
                            vaccineName,
                            availability
                        )
                        // passing this model class to our list
                        centerList = centerList + center
                    }
                    adapter = CenterAdapter(centerList)
                    vaccineRecyclerView.layoutManager = LinearLayoutManager(this)
                    vaccineRecyclerView.adapter = adapter
                    vaccineRecyclerView.visibility = View.VISIBLE
                } catch (e: JSONException) {
                    vaccine.visibility = View.GONE
                    enterYourPinTextView.visibility = View.GONE
                    vaccineNotFoundTextView.visibility = View.VISIBLE
                    delivery.visibility = View.VISIBLE
                    e.printStackTrace()
                }
            },
            { error ->
                vaccine.visibility = View.GONE
                enterYourPinTextView.visibility = View.GONE
                vaccineNotFoundTextView.visibility = View.VISIBLE
                delivery.visibility = View.GONE
                Toast.makeText(this, "Fail to get the Data", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(request)
    }

    private fun closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        val view = this.currentFocus

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {
            // now assign the system
            // service to InputMethodManager
            val manager: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }
}
