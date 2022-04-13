package com.android.main

import androidx.lifecycle.MutableLiveData
import com.llj.baselib.IOTViewModel

class MainVM:IOTViewModel() {

    val isNormal = MutableLiveData<Boolean>(false)

    fun openDoor(){
        sendOrderToDevice("C")
    }
    fun setPwd(num:String){
        sendOrderToDevice("B:${num}")
    }
    fun openKeyBoard(){
        sendOrderToDevice("A")
    }

    val devState = MutableLiveData<WebSocketType>()

}