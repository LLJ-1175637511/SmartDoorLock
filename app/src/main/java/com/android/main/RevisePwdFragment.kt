package com.android.main

import androidx.fragment.app.activityViewModels
import com.android.main.databinding.FragmentRevisePwdBinding
import com.llj.baselib.IOTViewModel
import com.llj.baselib.utils.ToastUtils

class RevisePwdFragment:BaseDialog<FragmentRevisePwdBinding>(){

    override fun getLayoutId() = R.layout.fragment_revise_pwd

    private val vm by activityViewModels<MainVM>()

    override fun initCreateView() {
        super.initCreateView()
        mDataBinding.btRevise.setOnClickListener {
            val pwd1 = mDataBinding.etNewPwd1.text.toString()
            val pwd2 = mDataBinding.etNewPwd2.text.toString()
            if (pwd1.isEmpty() || pwd2.isEmpty()){
                ToastUtils.toastShort("输入不能为空")
                return@setOnClickListener
            }
            if (pwd1 != pwd2){
                ToastUtils.toastShort("两次密码不一致")
                return@setOnClickListener
            }
            if (vm.devState.value != IOTViewModel.WebSocketType.DEVICE_ONLINE){
                ToastUtils.toastShort("密码修改失败 设备已离线")
                return@setOnClickListener
            }
            vm.setPwd(pwd1)
            destroyDialog()
        }
        mDataBinding.ivClose.setOnClickListener {
            destroyDialog()
        }
    }

}