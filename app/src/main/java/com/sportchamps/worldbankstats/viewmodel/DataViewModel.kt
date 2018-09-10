package com.sportchamps.worldbankstats.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.sportchamps.worldbankstats.model.UserRepository
import com.sportchamps.worldbankstats.model.data.pojo.WorldData




/*
 * Android architecture component and  the middleman
 * between view and model(or repository)
 * Here actions are recievec from View and correspsonding
  * action is taken to handle like getting information fom Model
 */
class DataViewModel : ViewModel(), UserRepository.MVInteractionInterface {

    val TAG:String = "SportChampsD-DVM"
    var worldInfoLiveData = MutableLiveData<WorldData>()
    var progressLiveData = MutableLiveData<Int>()

    var userRepository = UserRepository.getInstance(this)

    fun loadData(year:Int){
        Log.i(TAG, " Load world data")
        userRepository?.loadWorldData(year)
    }

    fun getWorldInfoLiveData():LiveData<WorldData>{
        return worldInfoLiveData
    }

    fun getProgressLiveData():LiveData<Int>{
        return progressLiveData
    }

    override fun onJobCompleted(updatedViewData: WorldData) {
        Log.i(TAG, " Model notified Viewmodel of updated data")
        worldInfoLiveData.postValue(updatedViewData)
    }

    override fun onProgressChange(progress: Int) {
        progressLiveData.postValue(progress)
    }
}