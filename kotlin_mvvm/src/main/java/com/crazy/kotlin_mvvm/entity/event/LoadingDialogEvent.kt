package com.crazy.kotlin_mvvm.entity.event

import com.crazy.kotlin_mvvm.BaseApplication.Companion.context
import com.crazy.kotlin_mvvm.R

/**
 * Created by wtc on 2019/11/15
 */
data class LoadingDialogEvent(var isLoading: Boolean, var message: String = context.getString(R.string.loading))