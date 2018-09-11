package com.sportchamps.worldbankstats.common.util
import com.google.gson.Gson
import com.sportchamps.worldbankstats.model.data.pojo.Country
import com.sportchamps.worldbankstats.model.data.pojo.PageHeader
import com.sportchamps.worldbankstats.model.data.pojo.RegionD
import com.sportchamps.worldbankstats.model.data.pojo.worldgdp.WorldGdp

import org.junit.Assert.*
import org.junit.Test
import java.io.InputStream
import java.util.*

class CommonUtilTest {
    //Dummy Network Data i/p
    var headerPassingJson:StringBuffer = StringBuffer("""[{"page":1,"pages":1,"per_page":"50","total":1},[{"id":"IND","iso2Code":"IN","name":"India","region":{"id":"SAS","iso2code":"8S","value":"South Asia"},"adminregion":{"id":"SAS","iso2code":"8S","value":"South Asia"},"incomeLevel":{"id":"LMC","iso2code":"XN","value":"Lower middle income"},"lendingType":{"id":"IBD","iso2code":"XF","value":"IBRD"},"capitalCity":"New Delhi","longitude":"77.225","latitude":"28.6353"}]"]""")
    var headerFailingJson:StringBuffer = StringBuffer("""[{"page":1,"pages":1,"per_page":"50",[{"id":"IND","iso2Code":"IN","name":"India","region":{"id":"SAS","iso2code":"8S","value":"South Asia"},"adminregion":{"id":"SAS","iso2code":"8S","value":"South Asia"},"incomeLevel":{"id":"LMC","iso2code":"XN","value":"Lower middle income"},"lendingType":{"id":"IBD","iso2code":"XF","value":"IBRD"},"capitalCity":"New Delhi","longitude":"77.225","latitude":"28.6353"}]"]""")
    var networkDataJson:StringBuffer = StringBuffer("""[{"page":1,"pages":1,"per_page":"50","total":1},[{"id":"IND","iso2Code":"IN","name":"India","region":{"id":"SAS","iso2code":"8S","value":"South Asia"},"adminregion":{"id":"SAS","iso2code":"8S","value":"South Asia"},"incomeLevel":{"id":"LMC","iso2code":"XN","value":"Lower middle income"},"lendingType":{"id":"IBD","iso2code":"XF","value":"IBRD"},"capitalCity":"New Delhi","longitude":"77.225","latitude":"28.6353"}]]""")
    var networkIncorectDataJson:StringBuffer = StringBuffer("""[{"page":1,"pages":1,"per_page":"50","total":1},[{me":"India","region":{"id":"SAS","iso2code":"8S","value":"South Asia"},"adminregion":{"id":"SAS","iso2code":"8S","value":"South Asia"},"incomeLevel":{"id":"LMC","iso2code":"XN","value":"Lower middle income"},"lendingType":{"id":"IBD","iso2code":"XF","value":"IBRD"},"capitalCity":"New Delhi","longitude":"77.225","latitude":"28.6353"}]]""")
    var correctFixedJson:StringBuffer = StringBuffer("""{ "countriesGdpData":[{"id":"IND","iso2Code":"IN","name":"India","region":{"id":"SAS","iso2code":"8S","value":"South Asia"},"adminregion":{"id":"SAS","iso2code":"8S","value":"South Asia"},"incomeLevel":{"id":"LMC","iso2code":"XN","value":"Lower middle income"},"lendingType":{"id":"IBD","iso2code":"XF","value":"IBRD"},"capitalCity":"New Delhi","longitude":"77.225","latitude":"28.6353"}]}""")
    var fixedJsonGdpData:StringBuffer = StringBuffer("""{"countriesGdpData":[{"id":"IND","iso2Code":"IN","name":"India","region":{"id":"SAS","iso2code":"8S","value":"South Asia"},"adminregion":{"id":"SAS","iso2code":"8S","value":"South Asia"},"incomeLevel":{"id":"LMC","iso2code":"XN","value":"Lower middle income"},"lendingType":{"id":"IBD","iso2code":"XF","value":"IBRD"},"capitalCity":"New Delhi","longitude":"77.225","latitude":"28.6353"}]}""")
    var fixedJsonWrongGdpData:StringBuffer = StringBuffer("""{gion":{"id":"SAS","iso2code":"8S","value":"South Asia"},"adminregion":{"id":"SAS","iso2code":"8S","value":"South Asia"},"incomeLevel":{"id":"LMC","iso2code":"XN","value":"Lower middle income"},"lendingType":{"id":"IBD","iso2code":"XF","value":"IBRD"},"capitalCity":"New Delhi","longitude":"77.225","latitude":"28.6353"}]}""")


    @Test
    fun test_getPageHeader_passCorrectInfo(){
        var exceptionResult = false
        var headerString = ""
        var index = 1
        while(headerPassingJson[index] != '[' ){
            index++
        }
        headerString = headerPassingJson.subSequence(1,index-1) as String
        val gson = Gson()
        try {
            var pageHeader: PageHeader = gson.fromJson(headerString, PageHeader::class.java)
        }catch (ex:Exception){
            ex.printStackTrace()
            exceptionResult = true
        }
        assertEquals(false, exceptionResult)
    }

    @Test
    fun test_getPageHeader_passWrongData(){
        var exceptionResult:Boolean = false
        var headerString = ""
        var index = 1
        while(headerFailingJson[index] != '[' ){
            index++
        }
        headerString = headerFailingJson.subSequence(1,index-1) as String
        val gson = Gson()
        try {
            var pageHeader: PageHeader = gson.fromJson(headerString, PageHeader::class.java)
        }catch (ex:Exception){
            ex.printStackTrace()
            exceptionResult = true
        }
        assertEquals(true, exceptionResult)
    }


    @Test
    fun test_fixJsonGetData_withCorrectNetworkJsonData(){
        val missingHeadJson = "{ "+
                "\"countriesGdpData\":"
        var tempStringBuffer = StringBuffer(networkDataJson)
        var missingTailJson = "}"
        var index = 1
        while(tempStringBuffer[index] != '[' ){
            index++
        }
        tempStringBuffer.delete(0,index)
        tempStringBuffer.insert(0,missingHeadJson)
        index =  tempStringBuffer.length-1
        while(tempStringBuffer[index] != ']'){
            index--
        }
        tempStringBuffer.replace(index , index+1,missingTailJson)

        assertEquals(correctFixedJson.toString(),tempStringBuffer.toString())
    }


    @Test
    fun test_fixJsonGetData_withInCorrectNetworkJsonData(){
        val missingHeadJson = "{ "+
                "\"countriesGdpData\":"
        var tempStringBuffer = StringBuffer(networkIncorectDataJson)
        var missingTailJson = "}"
        var index = 1
        while(tempStringBuffer[index] != '[' ){
            index++
        }
        tempStringBuffer.delete(0,index)
        tempStringBuffer.insert(0,missingHeadJson)
        index =  tempStringBuffer.length-1
        while(tempStringBuffer[index] != ']'){
            index--
        }
        tempStringBuffer.replace(index , index+1,missingTailJson)

        assertNotEquals(correctFixedJson.toString() , tempStringBuffer.toString())
    }

    @Test
    fun test_shortGdpDenotion_withMillionValue(){
        val dollarSign = "$ "
        val gdpNum = 1823689.0
        val expectedString = dollarSign+1.823689+" M"
        var retVal:String

        if(gdpNum > CommonUtil.million && gdpNum < CommonUtil.billion){
            retVal = (dollarSign+(gdpNum/ CommonUtil.million).toString() +" M")
        }else if(gdpNum > CommonUtil.billion && gdpNum < CommonUtil.trillion){
            retVal = (dollarSign+(gdpNum/ CommonUtil.billion).toString() +" B")
        }else if(gdpNum > CommonUtil.trillion){
            retVal = (dollarSign+(gdpNum/ CommonUtil.trillion).toString() +" T")
        }else{
            retVal = dollarSign+gdpNum.toString()
        }
        assertEquals(expectedString, retVal)
    }


    @Test
    fun test_shortGdpDenotion_MoreThan_1000Trillion(){
        val dollarSign = "$ "
        val gdpNum = 8772698646389470740497.0
        val expectedString = dollarSign+8772698646.389470740497+" T"
        var retVal:String

        if(gdpNum > CommonUtil.million && gdpNum < CommonUtil.billion){
            retVal = (dollarSign+(gdpNum/ CommonUtil.million).toString() +" M")
        }else if(gdpNum > CommonUtil.billion && gdpNum < CommonUtil.trillion){
            retVal = (dollarSign+(gdpNum/ CommonUtil.billion).toString() +" B")
        }else if(gdpNum > CommonUtil.trillion){
            retVal = (dollarSign+(gdpNum/ CommonUtil.trillion).toString() +" T")
        }else{
            retVal = dollarSign+gdpNum.toString()
        }
        assertEquals(expectedString, retVal)
    }

    @Test
    fun test_getSortedList_testGDP_Sorting(){
        var checkFlag = true
        var countryData1 = Country("INDIA", 200012323.0)
        var countryData2 = Country("CHINA", 38585109.0)
        var countryData3 = Country("PAKISTAN", 387873.0)
        var countryList1 = mutableListOf<Country>(countryData1,countryData2,countryData3)
        var regionGdp = 0.0
        countryList1.forEach { country ->  regionGdp += country.gdp }

        val regionD1:RegionD = RegionD("ASIA",countryList1, regionGdp)

        regionGdp = 0.0
        countryData1 = Country("FRANCE", 9872876358782.0)
        countryData2 = Country("ENGLAND", 32963982939.0)
        countryData3 = Country("ITALY", 98623986298362.0)
        val countryList2 = mutableListOf<Country>(countryData1,countryData2,countryData3)
        countryList2.forEach { country ->  regionGdp += country.gdp }
        val regionD2:RegionD = RegionD("EUROPE",countryList2,regionGdp)
        var regionDlist = mutableListOf<RegionD>(regionD1,regionD2)

        val sortType  = AppConstants.Companion.SORT_TYPE.GDP

        Collections.sort(regionDlist , object : Comparator<RegionD> {
            override fun compare(obj1: RegionD, obj2: RegionD): Int = when(sortType){
                AppConstants.Companion.SORT_TYPE.GDP->(obj1.gdp.compareTo(obj2.gdp))
                AppConstants.Companion.SORT_TYPE.ALPHABETICAL-> (obj1.name.compareTo(obj2.name))
            }
        })

        for(i in 0..(regionDlist.size-2)){
            if(regionDlist[i].gdp > regionDlist[i+1].gdp){
                checkFlag = false
                break
            }
        }
        assertTrue(checkFlag)
    }

    @Test
    fun test_getSortedList_testAlphabetical_Sorting(){
        var checkFlag = true
        var countryData1 = Country("INDIA", 200012323.0)
        var countryData2 = Country("CHINA", 38585109.0)
        var countryData3 = Country("PAKISTAN", 387873.0)
        var countryList1 = mutableListOf<Country>(countryData1,countryData2,countryData3)
        var regionGdp = 0.0
        countryList1.forEach { country ->  regionGdp += country.gdp }

        val regionD1:RegionD = RegionD("ASIA",countryList1, regionGdp)

        regionGdp = 0.0
        countryData1 = Country("FRANCE", 9872876358782.0)
        countryData2 = Country("ENGLAND", 32963982939.0)
        countryData3 = Country("ITALY", 98623986298362.0)
        val countryList2 = mutableListOf<Country>(countryData1,countryData2,countryData3)
        countryList2.forEach { country ->  regionGdp += country.gdp }
        val regionD2:RegionD = RegionD("EUROPE",countryList2,regionGdp)
        var regionDlist = mutableListOf<RegionD>(regionD1,regionD2)

        val sortType  = AppConstants.Companion.SORT_TYPE.ALPHABETICAL

        Collections.sort(regionDlist , object : Comparator<RegionD> {
            override fun compare(obj1: RegionD, obj2: RegionD): Int = when(sortType){
                AppConstants.Companion.SORT_TYPE.GDP->(obj1.gdp.compareTo(obj2.gdp))
                AppConstants.Companion.SORT_TYPE.ALPHABETICAL-> (obj1.name.compareTo(obj2.name))
            }
        })

        for(i in 0..(regionDlist.size-2)){
            if(regionDlist[i+1].name.compareTo(regionDlist[i].name) < 0){
                checkFlag = false
                break
            }
        }
        assertTrue(checkFlag)
    }


    @Test
    fun test_GetWorldGdpPojo_WithCorrectJson(){
        var noExceptionFlag = true
        val missingHeadGdpJson = "{ "+
                "\"countriesGdpData\":"
        val gson = Gson()
        var gsonData: WorldGdp? = null
        try {
            gsonData = gson.fromJson(fixedJsonGdpData.toString(), WorldGdp::class.java)
        }catch ( ex:Exception){
            ex.printStackTrace()
            noExceptionFlag = false
        }
       assertTrue(noExceptionFlag)
    }

    @Test
    fun test_GetWorldGdpPojo_WithWrongJson(){
        var exceptionFlag = false
        val missingHeadGdpJson = "{ "+
                "\"gkbkjbkdc\":"
        val gson = Gson()
        var gsonData: WorldGdp? = null
        try {
            gsonData = gson.fromJson(fixedJsonWrongGdpData.toString(), WorldGdp::class.java)
        }catch ( ex:Exception){
            ex.printStackTrace()
            exceptionFlag = true
        }
        assertTrue(exceptionFlag)
    }
}