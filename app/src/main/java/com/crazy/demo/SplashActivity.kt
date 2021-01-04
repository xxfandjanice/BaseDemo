package com.crazy.demo

import android.os.Handler
import com.crazy.demo.databinding.ActivitySplashBinding
import com.crazy.kotlin_mvvm.base.BaseActivity
import com.crazy.kotlin_mvvm.base.BaseViewModel
import com.crazy.kotlin_mvvm.ext.startKtxActivityAndFinish
import com.crazy.kotlin_mvvm.utils.StatusBarUtils

class SplashActivity : BaseActivity<ActivitySplashBinding, BaseViewModel>() {

    override fun getLayoutResId() = R.layout.activity_splash

    override fun initVariableId() = BR.viewModel

    override fun useToolBar() = false

    override fun useWhiteStatusBar() = false

    override fun initView() {
        StatusBarUtils.transparentStatusBar(this)
        StatusBarUtils.statusBarDarkLightMode(this, isDark = true, isFullscreen = true)
        Handler().postDelayed({
                startKtxActivityAndFinish<MainActivity>()
        }, 3000)
    }

    override fun initData() {}
}
