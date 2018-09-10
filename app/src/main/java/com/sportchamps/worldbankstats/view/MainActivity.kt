package com.sportchamps.worldbankstats.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.sportchamps.worldbankstats.viewmodel.DataViewModel
import com.sportchamps.worldbankstats.R
import com.sportchamps.worldbankstats.interfaces.ItemClickListener
import com.sportchamps.worldbankstats.model.data.pojo.RegionD
import java.io.Serializable
import android.support.v7.widget.DividerItemDecoration
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.sportchamps.worldbankstats.common.util.AppConstants
import com.sportchamps.worldbankstats.common.util.CommonUtil
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),ItemClickListener{
    val TAG:String = "SportChampsD-MActivity"
    lateinit var dataViewModel: DataViewModel
    lateinit var rv_worldData:RecyclerView
    lateinit var progressBar:ProgressBar
    lateinit var progressText:TextView
    lateinit var progressLayout:LinearLayout
    lateinit var selectedYearSpinner:Spinner
    lateinit var sortSpinner:Spinner
    var recyclerViewItemList = mutableListOf<RegionD>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        observeForViewDataChange()
        observeForProgressChange()
        dataViewModel.loadData((selectedYearSpinner.selectedItem.toString()).toInt())
    }

    override fun onResume() {
        super.onResume()
        setViewEventListener()
    }

    private fun initView(){
        rv_worldData = findViewById(R.id.rv_worlddata)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.pB_worlddata)
        progressText = findViewById(R.id.progressText)
        selectedYearSpinner = findViewById(R.id.yearSelect)
        sortSpinner = findViewById(R.id.sortView)
        rv_worldData.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        rv_worldData.addItemDecoration(DividerItemDecoration(rv_worldData.getContext(),
                DividerItemDecoration.VERTICAL))
        rv_worldData.adapter = MyRegionViewAdapter(this,this,
                recyclerViewItemList  )
    }

    private fun setViewEventListener(){
        val onItemSelectedListen:OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                Log.i(TAG, " Item is selected  for View ID : ${selectedItemView.id}")
                if(recyclerViewItemList.size >0 ) {
                    when (parentView.id) {
                        R.id.yearSelect -> {
                            Log.i(TAG, " Selected Year :  ${selectedYearSpinner.selectedItem}")
                            dataViewModel.loadData((selectedYearSpinner.selectedItem.toString()).toInt())
                        }
                        R.id.sortView -> {
                            sortViewItems(recyclerViewItemList, AppConstants.Companion.SORT_TYPE.values()[position])
                        }
                    }
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }
        selectedYearSpinner.onItemSelectedListener = onItemSelectedListen
        sortSpinner.onItemSelectedListener = onItemSelectedListen
    }

    private fun observeForViewDataChange(){
        dataViewModel.getWorldInfoLiveData().observe(this, Observer { worldData ->
            run {
                if (worldData == null) {
                    Log.i(TAG, " World Data is null")
                } else {
                    Log.i(TAG, "Progress View is  made hidden")
                    progressLayout.visibility = View.INVISIBLE
                    val sortOption = applicationContext.resources.getStringArray(R.array.sortOption)
                    val sortType = when(sortSpinner.selectedItem.toString()){
                            sortOption[1] ->  AppConstants.Companion.SORT_TYPE.ALPHABETICAL
                            else ->  AppConstants.Companion.SORT_TYPE.GDP
                    }
                    sortViewItems(worldData.regionList, sortType)
                }
            }
        })
    }

    /*
      Live data observer for Progress change when fetching
      data from Network
     */
    private fun observeForProgressChange(){
        dataViewModel.getProgressLiveData().observe(this, Observer { progressVal ->
            run {
                Log.i(TAG, "World data Network fetch updated, Progress : ${progressVal}")
                progressText.text = "$progressVal % completed"
            }
        }
        )
    }

    /*
     * Sort view items as per the Sort option selected
     * Avaiable options:
     *    GDP based
     *    Alphabetical ordering
     */
    private fun sortViewItems(itemsList: MutableList<RegionD>, sortType:AppConstants.Companion.SORT_TYPE ){
        Log.d(TAG, " Sort Type : "+sortType.name)
        var listCopy = mutableListOf<RegionD>()
        listCopy.addAll(itemsList)
        Log.i(TAG, " Sorting option : "+sortType.name)
        CommonUtil.getSortedList(listCopy ,sortType)
        recyclerViewItemList.clear()
        rv_worldData.adapter?.notifyDataSetChanged()
        recyclerViewItemList = listCopy
        rv_worldData.adapter = MyRegionViewAdapter(
                this,
                this@MainActivity,
                recyclerViewItemList
        )
    }

    override fun onItemClick(item: RegionD) {
        Log.i(TAG, " Region Item Clicked "+item.name+" Country data size : "+item.countryList.size)
        val countryActivityIntent:Intent = Intent(this,CountryViewActivity::class.java)
        countryActivityIntent.putExtra("Country_Data",item as Serializable)
        startActivity(countryActivityIntent)
    }
}
