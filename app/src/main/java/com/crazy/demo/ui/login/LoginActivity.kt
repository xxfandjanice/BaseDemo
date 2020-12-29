package com.crazy.demo.ui.login

import android.view.View
import com.crazy.demo.BR
import com.crazy.demo.R
import com.crazy.demo.databinding.ActivityLoginBinding
import com.crazy.demo.ui.login.viewModel.LoginViewModel
import com.crazy.kotlin_mvvm.base.BaseActivity
import com.crazy.kotlin_mvvm.listener.OnMyClickListener

class LoginActivity:BaseActivity<ActivityLoginBinding, LoginViewModel>(),
    OnMyClickListener<LoginViewModel> {


    override fun getLayoutResId() = R.layout.activity_login

    override fun initVariableId() = BR.viewModel

    override fun useToolBar() = false

    override fun initView() {

    }

    override fun initData() {

    }

    override fun onClick(view: View, model: LoginViewModel?) {
        when (view.id) {

        }
    }



}