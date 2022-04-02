package com.android.main

import android.app.Application
import com.llj.baselib.IOTLib
import com.llj.baselib.bean.UserConfigBean

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val bean = UserConfigBean(
            userId = "19204",
            appKey = "303e55ee4e",
            deviceId = "25850",
            clientId = "1210",
            clientSecret = "a4bd1bc7c7"
        )
        IOTLib.init(applicationContext,bean)
    }
}