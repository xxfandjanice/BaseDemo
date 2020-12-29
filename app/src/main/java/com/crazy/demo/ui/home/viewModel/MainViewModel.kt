package com.crazy.demo.ui.home.viewModel

import androidx.lifecycle.MutableLiveData
import com.crazy.biteleven.constant.SpConstant
import com.crazy.demo.net.RetrofitClient
import com.crazy.kotlin_mvvm.BaseApplication
import com.crazy.kotlin_mvvm.base.BaseViewModel
import com.crazy.kotlin_mvvm.ext.versionCode
import com.crazy.kotlin_mvvm.utils.Preference

class MainViewModel :BaseViewModel(){

    var pageIndex = MutableLiveData(0)

    var isLogin by Preference(SpConstant.IS_LOGIN, false)

    /**
     * APP版本升级检测
     */
    fun checkVersion() {
        launch(block = {
            apiCall(call = {
                RetrofitClient.apiService.checkVersion(version = BaseApplication.context.versionCode)
            })?.run {
                handleHttpResponse(this, httpSuccessBlock = {
                })
            }
        })
    }

}