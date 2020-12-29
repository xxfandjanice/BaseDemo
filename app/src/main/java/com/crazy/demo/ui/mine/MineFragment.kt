package com.crazy.demo.ui.mine

import android.view.View
import com.crazy.demo.BR
import com.crazy.demo.R
import com.crazy.demo.databinding.FragmentMineBinding
import com.crazy.demo.ui.mine.viewModel.MineViewModel
import com.crazy.kotlin_mvvm.base.LazyBaseFragment
import com.crazy.kotlin_mvvm.listener.OnMyClickListener

class MineFragment: LazyBaseFragment<FragmentMineBinding, MineViewModel>(), OnMyClickListener<MineViewModel> {

    override fun getLayoutResId() = R.layout.fragment_mine

    override fun initVariableId() = BR.viewModel

    override fun initView() {
        binding.clickListener = this
    }

    override fun initData() {
    }

    override fun onClick(view: View, model: MineViewModel?) {
        when(view.id){

        }
    }
}