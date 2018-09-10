package com.sportchamps.worldbankstats.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sportchamps.worldbankstats.R
import com.sportchamps.worldbankstats.interfaces.ItemClickListener
import com.sportchamps.worldbankstats.model.data.pojo.RegionD
import com.sportchamps.worldbankstats.common.util.CommonUtil


class MyRegionViewAdapter(val context: Context, val itemClickListener:
                            ItemClickListener,var regionList:MutableList<RegionD> ):
                            RecyclerView.Adapter<MyRegionViewAdapter.ViewHolder>() {
    val TAG = "SportChampsD-MrvAdapter"
    lateinit var regionNameTv:TextView
    lateinit var regionGdpTv:TextView
    val mInflater: LayoutInflater

    init{
        mInflater = LayoutInflater.from(context)
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        init {
            regionNameTv = itemView.findViewById(R.id.regionname)
            regionGdpTv = itemView.findViewById(R.id.regiongdp)
        }

        fun bind(regionD:RegionD, itemClickListener: ItemClickListener){
            itemView.setOnClickListener { itemClickListener.onItemClick(regionD) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyRegionViewAdapter.ViewHolder {
        val view = mInflater.inflate(R.layout.item_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyRegionViewAdapter.ViewHolder, elementPos: Int) {
        regionNameTv.text = regionList[elementPos].name
        regionGdpTv.text = CommonUtil.shortGdpDenotion(regionList[elementPos].gdp)
        viewHolder.bind(regionList[elementPos],itemClickListener)
    }

    override fun getItemCount(): Int {
        return regionList.size
    }
}