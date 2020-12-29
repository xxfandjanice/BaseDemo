package com.crazy.demo

import com.crazy.kotlin_mvvm.BaseApplication
import com.uuzuche.lib_zxing.activity.ZXingLibrary

class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        ZXingLibrary.initDisplayOpinion(this)
    }


}