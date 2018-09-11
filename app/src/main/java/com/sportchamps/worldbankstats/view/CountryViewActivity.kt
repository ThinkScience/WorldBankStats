package com.sportchamps.worldbankstats.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.sportchamps.worldbankstats.R
import com.sportchamps.worldbankstats.common.util.AppConstants
import com.sportchamps.worldbankstats.common.util.CommonUtil
import com.sportchamps.worldbankstats.model.data.pojo.Country
import com.sportchamps.worldbankstats.model.data.pojo.RegionD

/*
  Module representing Countries data under the region
 */
class CountryViewActivity: AppCompatActivity() {
    val TAG:String = "SportChampsD-CActivity"
    private var regionData:RegionD? = null
    private var rvCountryList:MutableList<Country> = mutableListOf()
    private lateinit var rv_countryData: RecyclerView
    lateinit var sortView: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.country_activity_main)
        regionData = intent?.extras?.get("Country_Data") as RegionD?
        if(null != regionData)
           rvCountryList.addAll(regionData!!.countryList)
        initActivityView()
    }

    override fun onResume() {
        super.onResume()
        setViewEventListener()
    }

    fun initActivityView(){
        sortView = findViewById(R.id.sortspinnercountries)
        rv_countryData = findViewById(R.id.rv_countrydata)
        rv_countryData.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        rv_countryData.adapter = MyCountryViewAdapter(this,null
                ,rvCountryList  )
        rv_countryData.addItemDecoration(DividerItemDecoration(rv_countryData.getContext(),
                DividerItemDecoration.VERTICAL))
    }

    private fun setViewEventListener(){
        val onItemSelectedListen: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                Log.i(TAG, " Item is selected  for View ID : ${selectedItemView.id}")
                when(parentView.id){
                    R.id.sortspinnercountries -> {
                        sortViewItems(AppConstants.Companion.SORT_TYPE.values()[position])
                    }
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }
        }
        sortView.onItemSelectedListener = onItemSelectedListen
    }

    private fun sortViewItems(sortType:AppConstants.Companion.SORT_TYPE ){
        Log.d(TAG, " Sort Type : "+sortType.name)
        var listCopy = mutableListOf<Country>()
        listCopy.addAll(rvCountryList)
        Log.i(TAG, " Sorting option : "+sortType.name)
        sortListData(listCopy ,sortType)
        rvCountryList.clear()
        rv_countryData.adapter?.notifyDataSetChanged()
        rvCountryList = listCopy
        rv_countryData.adapter = MyCountryViewAdapter(
                this,
                null,
                rvCountryList
        )
    }

    fun sortListData(list: MutableList<Country>,sortType: AppConstants.Companion.SORT_TYPE){
        list.sortWith(object: Comparator<Country>{
            override fun compare(c1: Country, c2: Country): Int = when(sortType){
                    AppConstants.Companion.SORT_TYPE.GDP->(c1.gdp.compareTo(c2.gdp))
                    AppConstants.Companion.SORT_TYPE.ALPHABETICAL-> (c1.name.compareTo(c2.name))
            }
        })
    }
}