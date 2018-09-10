package com.sportchamps.worldbankstats.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.sportchamps.worldbankstats.R
import com.sportchamps.worldbankstats.interfaces.ItemClickListener
import com.sportchamps.worldbankstats.model.data.pojo.Country
import com.sportchamps.worldbankstats.common.util.CommonUtil


class MyCountryViewAdapter(val context: Context, var itemClickListener: ItemClickListener?,
                           var countryList:List<Country> ):
                            RecyclerView.Adapter<MyCountryViewAdapter.ViewHolder>() {
    lateinit var regionNameTv: TextView
    lateinit var regionGdpTv: TextView
    val mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(context)
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            regionNameTv = itemView.findViewById(R.id.regionname)
            regionGdpTv = itemView.findViewById(R.id.regiongdp)
        }

        fun bind(country: Country, itemClickListener: ItemClickListener?) {
            itemView.setOnClickListener {
                Toast.makeText(itemView.context," Item : ${country.name} " +
                        "view got clicked",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyCountryViewAdapter.ViewHolder {
        val view = mInflater.inflate(R.layout.item_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyCountryViewAdapter.ViewHolder, elementPos: Int) {
        regionNameTv.text = countryList[elementPos].name
        regionGdpTv.text = CommonUtil.shortGdpDenotion(countryList[elementPos].gdp)
        viewHolder.bind(countryList[elementPos], itemClickListener)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
