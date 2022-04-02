package com.android.main

import androidx.lifecycle.MutableLiveData
import com.llj.baselib.IOTViewModel

class MainVM:IOTViewModel() {

    fun openDoor(){
        sendOrderToDevice("")
    }
    fun setPwd(num:String){
        sendOrderToDevice(num)
    }
    fun openKeyBoard(){
        sendOrderToDevice("")
    }

    val devState = MutableLiveData<WebSocketType>()

}