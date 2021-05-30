package com.workfromhome.checkvac.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.workfromhome.checkvac.Adapters.StateAdapter
import com.workfromhome.checkvac.Models.StatesModel
import com.workfromhome.checkvac.R
import kotlinx.android.synthetic.main.activity_covid_cases.*
import org.json.JSONException

class CovidCasesActivity : AppCompatActivity() {
    lateinit var stateRecyclerView: RecyclerView
    lateinit var adapter: StateAdapter
    lateinit var stateList: List<StatesModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_covid_cases)
        stateList = ArrayList<StatesModel>()
        getStateInfo()
        getWorldInfo()
    }
    private fun getStateInfo() {
        val url = "https://api.rootnet.in/covid19-in/stats/latest"
        val queue = Volley.newRequestQueue(this@CovidCasesActivity)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val dataObj = response.getJSONObject("data")
                    val summaryObj =
                        dataObj.getJSONObject("summary") // dataObj.--//-- because we have summary inside the dataObj
                    val cases: Int = summaryObj.getInt("total")
                    val recovered: Int = summaryObj.getInt("discharged")
                    val deaths: Int = summaryObj.getInt("deaths")

                    indianCasesTextView.text = cases.toString()
                    indianRecoveredsTextView.text = recovered.toString()
                    indianDeathsTextView.text = deaths.toString()

                    val regionalArray = dataObj.getJSONArray("regional")
                    for (i in 0 until regionalArray.length()) {
                        val regionalObj = regionalArray.getJSONObject(i)
                        val stateName: String = regionalObj.getString("loc")
                        val cases: Int = regionalObj.getInt("totalConfirmed")
                        val deaths: Int = regionalObj.getInt("deaths")
                        val recovered: Int = regionalObj.getInt("discharged")

                        val stateModel = StatesModel(stateName, recovered, deaths, cases)
                        stateList = stateList + stateModel
                    }
                    adapter = StateAdapter(stateList)
                    stateRecyclerView = recyclerViewStates
                    stateRecyclerView.layoutManager = LinearLayoutManager(this)
                    stateRecyclerView.adapter = adapter
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                {
                    Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }
        )
        queue.add(request)
    }

    private fun getWorldInfo() {
        val url = "https://corona.lmao.ninja/v3/covid-19/all"
        val queue = Volley.newRequestQueue(this@CovidCasesActivity)
        val request =
            JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        val worldCases: Int = response.getInt("cases")
                        val worldRecovered: Int = response.getInt("recovered")
                        val worldDeaths: Int = response.getInt("deaths")

                        worldRecoveredTextView.text = worldRecovered.toString()
                        worldCasesTextView.text = worldCases.toString()
                        worldDeathsTextView.text = worldDeaths.toString()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    Toast.makeText(this, "Failed to fetch Data", Toast.LENGTH_SHORT).show()
                }
            )
        queue.add(request)
    }
}
