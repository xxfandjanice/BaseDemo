package com.crazy.kotlin_mvvm.base

import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.crazy.kotlin_mvvm.R
import com.crazy.kotlin_mvvm.databinding.ActivityBaseBinding
import com.crazy.kotlin_mvvm.databinding.LayoutToolbarBinding
import com.crazy.kotlin_mvvm.listener.PermissionListener
import com.crazy.kotlin_mvvm.utils.ActivityManager
import com.crazy.kotlin_mvvm.utils.LocaleManager
import com.crazy.kotlin_mvvm.utils.StatusBarUtils
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 * Created by wtc on 2019/11/2
 */
abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    private val CODE_REQUEST_PERMISSION = 9999
    var mPermissionListener: PermissionListener? = null

    private val baseBinding by lazy {
        DataBindingUtil.setContentView<ActivityBaseBinding>(
            this,
            R.layout.activity_base
        )
    }
    protected val toolbarBinding: LayoutToolbarBinding? by lazy {
        if (useToolBar()) {
            DataBindingUtil.inflate<LayoutToolbarBinding>(
                LayoutInflater.from(this),
                R.layout.layout_toolbar,
                baseBinding.container,
                true
            )
        } else null
    }
    protected val binding: V by lazy {
        DataBindingUtil.inflate<V>(
            LayoutInflater.from(this), getLayoutResId(), baseBinding.container,
            true
        )
    }

    protected lateinit var viewModel: VM

    var loadingDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.getInstance().addActivity(this)
        initToolBar()

        binding.lifecycleOwner = this

        initViewModel()

        //dataBing和viewModel绑定
        binding.setVariable(initVariableId(), viewModel)

        if (useEventBus()) EventBus.getDefault().register(this)

        startObserver()

        //初始化View
        initView()
        //初始化数据
        initData()

        setWhiteStatusBar()
    }


    /**
     * 根据泛型自动创建ViewModel对象
     */
    open fun initViewModel() {
        val type = this::class.java.genericSuperclass
        val viewModelClass: Class<VM>
        if (type is ParameterizedType) {
            viewModelClass = type.actualTypeArguments[1] as Class<VM>
            viewModel = ViewModelProviders.of(this).get(viewModelClass)

            //ViewModel感知页面生命周期变化
            lifecycle.addObserver(viewModel)
        }
    }

    /**
     * 接收ViewModel层来的数据变化
     */
    open fun startObserver() {
        viewModel.isLoading.observe(this, Observer {
            if (it.isLoading) loadingDialog = ProgressDialog.show(this, getString(R.string.tip), it.message)
            else loadingDialog?.dismiss()
        })
    }

    open fun initToolBar() {
        StatusBarUtils.setColor(this, resources.getColor(R.color.white))
        StatusBarUtils.statusBarDarkLightMode(this, isDark = true, isFullscreen = false)
        if (useToolBar()) {
            setSupportActionBar(toolbarBinding?.toolbar)
            toolbarBinding?.toolbar?.setNavigationOnClickListener { finish() }
        }
    }

    //设置白底黑字模式
    open fun setWhiteStatusBar() {
        if (useWhiteStatusBar()) {
            StatusBarUtils.setColor(this, resources.getColor(R.color.white))
            StatusBarUtils.statusBarDarkLightMode(this, isDark = true, isFullscreen = false)
        }
    }


    /**
     * 默认不使用 EventBus
     */
    open fun useEventBus() = false

    open fun useToolBar() = true

    open fun useWhiteStatusBar() = true

    abstract fun getLayoutResId(): Int

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun initVariableId(): Int

    abstract fun initView()

    abstract fun initData()


    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) EventBus.getDefault().unregister(this)
        ActivityManager.getInstance().removeActivity(this)
    }

//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(LocaleManager.setLocale(newBase!!))
//        LocaleManager.changeAppLanguage(this)
//    }

    /**
     * 申请权限
     * @param permissions 需要申请的权限(数组)
     * @param listener 权限回调接口
     */
    fun requestPermissions(permissions: Array<String>, listener: PermissionListener?) {
        mPermissionListener = listener
        val permissionList = ArrayList<String>()
        for (permission in permissions) {
            //权限没有授权
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            )
                permissionList.add(permission)
        }

        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionList.toTypedArray(),
                CODE_REQUEST_PERMISSION
            )
        } else mPermissionListener?.onGranted()
    }


    /**
     * 权限申请结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CODE_REQUEST_PERMISSION -> if (grantResults.isNotEmpty()) {
                val deniedPermissions = ArrayList<String>()
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        val permission = permissions[i]
                        deniedPermissions.add(permission)
                    }
                }

                if (deniedPermissions.isEmpty())
                    mPermissionListener?.onGranted()
                else
                    mPermissionListener?.onDenied(deniedPermissions)
            }
        }
    }
}