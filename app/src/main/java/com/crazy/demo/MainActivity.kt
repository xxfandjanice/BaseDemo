package com.crazy.demo

import android.Manifest
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.crazy.demo.constant.PageConstant
import com.crazy.kotlin_mvvm.adapter.MyFragmentPagerAdapter
import com.crazy.kotlin_mvvm.base.BaseActivity
import com.crazy.kotlin_mvvm.entity.event.LogoutEvent
import com.crazy.kotlin_mvvm.ext.toast
import com.crazy.kotlin_mvvm.listener.OnMyClickListener
import com.crazy.demo.databinding.ActivityMainBinding
import com.crazy.demo.ui.home.fragment.HomeFragment
import com.crazy.demo.ui.home.viewModel.MainViewModel
import com.crazy.demo.ui.login.LoginActivity
import com.crazy.demo.ui.mine.MineFragment
import com.crazy.kotlin_mvvm.entity.event.LoginEvent
import com.crazy.kotlin_mvvm.ext.startKtxActivity
import com.crazy.kotlin_mvvm.utils.StatusBarUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),
    OnMyClickListener<MainViewModel> {

    private var exitTime: Long = 0

    private val fragments by lazy { arrayListOf<Fragment>() }

    override fun getLayoutResId() = R.layout.activity_main

    override fun initVariableId() = BR.viewModel

    override fun useEventBus() = true

    override fun useToolBar() = false

    override fun initView() {
        StatusBarUtils.setTransparentForImageView(this, null)
        binding.clickListener = this
        viewModel.pageIndex.value = 0

        fragments.add(HomeFragment())
        fragments.add(MineFragment())

        binding.viewPager.adapter = MyFragmentPagerAdapter(supportFragmentManager, fragments)
        binding.viewPager.offscreenPageLimit = fragments.size
        //申请权限
        requestPermission()
    }

    override fun initData() {}

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventBus(any: Any) {
        when (any) {
            //登录成功
            is LoginEvent -> {
                viewModel.isLogin =true
            }

            //退出登录
            is LogoutEvent -> {
                viewModel.isLogin =false
                binding.tabHome.performClick()
                startKtxActivity<LoginActivity>()
            }
        }
    }

    override fun onClick(view: View, model: MainViewModel?) {

        viewModel.pageIndex.value = when (view.id) {
            R.id.tab_home -> 0
            R.id.tab_mine -> 1
            else -> 0
        }
        if (!viewModel.isLogin && viewModel.pageIndex.value != 0) {
            viewModel.pageIndex.value = 0
            startKtxActivity<LoginActivity>()
            return
        }
        binding.viewPager.setCurrentItem(viewModel.pageIndex.value!!, false)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.run {
            val index = getInt(PageConstant.DATA, 0)
            chang2Page(index)
        }
    }


    private fun chang2Page(index: Int) {
        when (index) {
            0 -> binding.tabHome.performClick()
            1 -> binding.tabMine.performClick()
        }
    }

    /**
     * 请求权限
     */
    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), null
        )
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                toast(this, R.string.one_again_exit)
                exitTime = System.currentTimeMillis()
            } else finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
