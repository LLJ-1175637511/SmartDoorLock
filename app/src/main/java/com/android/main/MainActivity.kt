package com.android.main

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.android.main.databinding.ActivityMainBinding
import com.llj.baselib.IOTLib
import com.llj.baselib.IOTViewModel
import com.llj.baselib.save
import com.llj.baselib.ui.IOTMainActivity
import com.llj.baselib.utils.LogUtils
import com.llj.baselib.utils.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : IOTMainActivity<ActivityMainBinding>() {

    override fun getLayoutId() = R.layout.activity_main

    private val vm by viewModels<MainVM>()

    private var count = 0
    private val errString = "今日报警次数："

    override fun init() {
        super.init()
        vm.connect(this, MainDataBean::class.java)
        initMainView()
    }

    override fun onResume() {
        super.onResume()
        count = IOTLib.getSP(SP_NOTIFICATION).getInt(SP_NOTIFICATION_COUNT, 2)
        mDataBinding.tvCountError.text = "${errString}${count}"
    }

    private fun initMainView() {
        initToolbar()
        mDataBinding.btOpenDoor.setOnClickListener {
            vm.openDoor()
        }
        mDataBinding.btOpen.setOnClickListener {
            vm.openKeyBoard()
        }
        mDataBinding.btRevise.setOnClickListener {
            showDialog(RevisePwdFragment(), "revise")
        }
    }

    private fun initToolbar() {
        mDataBinding.toolbar.apply {
            toolbarBase.inflateMenu(R.menu.toolbar_menu)
            toolbarBase.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.quit_app -> {
                        startCommonActivity<LoginActivity>()
                        finish()
                    }
                }
                false
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun offDevLine() {
        mDataBinding.tvDevState.setTextColor(R.color.red)
        mDataBinding.tvDevState.text = "设备离线"
    }

    @SuppressLint("ResourceAsColor")
    override fun onDevLine() {
        mDataBinding.tvDevState.setTextColor(R.color.greenDark)
        mDataBinding.tvDevState.text = "设备在线"
    }

    override fun realData(data: Any?) {
        if (data == null) return
        val mainBean = data as MainDataBean
        runOnUiThread {
            if (mainBean.isHavePeople == 1){
                mDataBinding.tvIsHavePeople.text = "请注意！ 门口有人"
            }else{
                mDataBinding.tvIsHavePeople.text = "正常"
            }
        }
        if (mainBean.isHavePeopleAlert == 1){
            buildNotification("门口有可疑人员")
        }
        if (mainBean.isPwdErrorAlert == 1){
            buildNotification("密码多次输入失败 自动禁用")
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SmartDoorLockNotification"
            val descriptionText = "智能门锁警报"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(t: String) {
        createNotificationChannel()
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 10, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle("门锁管家")
            .setContentText(t)
            .setShowWhen(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        NotificationManagerCompat.from(this).notify(100, builder.build())
        //更新警报次数
        IOTLib.getSP(SP_NOTIFICATION).save {
            putInt(SP_NOTIFICATION_COUNT, count + 1)
        }
    }

    override fun webState(state: IOTViewModel.WebSocketType) {
        vm.devState.postValue(state)
    }

    private fun showDialog(df: DialogFragment, tag: String) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val prev: Fragment? = supportFragmentManager.findFragmentByTag(tag)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        df.show(ft, tag)
    }

    companion object {
        const val CHANNEL_ID = "686421"
        const val SP_NOTIFICATION = "SP_NOTIFICATION"
        const val SP_NOTIFICATION_COUNT = "SP_NOTIFICATION_COUNT"
    }
}