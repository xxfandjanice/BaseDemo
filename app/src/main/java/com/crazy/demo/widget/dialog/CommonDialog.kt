package com.crazy.demo.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import com.crazy.demo.R
import com.crazy.demo.databinding.LayoutDialogCommonBinding
import com.crazy.kotlin_mvvm.ext.layoutInflater

class CommonDialog(context: Context,themeRedId:Int) : Dialog(context,themeRedId) {

    class Builder(private val context: Context) {
        private var title: String? = null
        private var message: String? = null
        private var positiveStr: String? = null
        private var negativeStr: String? = null
        private var onlyPositiveBtn: Boolean = false
        private var canceledOnTouchOutside: Boolean = true
        private var positiveBtnColorRes: Int = context.resources.getColor(R.color.theme)
        private var positiveBtnOnClickListener: (() -> Unit)? = null
        private var negativeBtnOnClickListener: (() -> Unit)? = null

        fun setTitle(title: String?):Builder{
            this.title = title
            return this
        }

        fun setMessage(message: String?):Builder{
            this.message = message
            return this
        }

        fun setPositiveBtnColor(positiveBtnColorRes: Int):Builder{
            this.positiveBtnColorRes = positiveBtnColorRes
            return this
        }

       fun setOnlyPositiveBtn(onlyPositiveBtn: Boolean):Builder{
            this.onlyPositiveBtn = onlyPositiveBtn
            return this
        }

        fun setPositiveBtnOnClickListener(listener: (()->Unit)?):Builder{
            this.positiveBtnOnClickListener = listener
            return this
        }

       fun setNegativeBtnOnClickListener(listener: (()->Unit)?):Builder{
            this.negativeBtnOnClickListener = listener
            return this
        }

       fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean):Builder{
            this.canceledOnTouchOutside = canceledOnTouchOutside
            return this
        }

        fun create():CommonDialog{

            val dialog = CommonDialog(context, R.style.CommonDialogTheme)

            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)

            val dialogBinding = DataBindingUtil.inflate<LayoutDialogCommonBinding>(
                context.layoutInflater!!,
                R.layout.layout_dialog_common,
                null,
                false
            )

            dialogBinding.tvTitle.text = title?:context.resources.getString(R.string.tip)
            dialogBinding.tvMessage.text = message?:""
            dialogBinding.tvPositive.text = positiveStr?:context.resources.getString(R.string.sure)
            dialogBinding.tvPositive.setTextColor(positiveBtnColorRes)
            dialogBinding.tvNegative.text = negativeStr?:context.resources.getString(R.string.cancel)
            dialogBinding.tvNegative.visibility = if (onlyPositiveBtn) View.GONE else View.VISIBLE
            dialogBinding.divideLine.visibility = if (onlyPositiveBtn) View.GONE else View.VISIBLE
            dialogBinding.tvPositive.setOnClickListener {
                dialog.dismiss()
                positiveBtnOnClickListener?.invoke()
            }
            dialogBinding.tvNegative.setOnClickListener {
                dialog.dismiss()
                negativeBtnOnClickListener?.invoke()
            }
            dialog.setContentView(dialogBinding.root)

            val dialogWindow = dialog.window
            val windowManager = dialogWindow!!.windowManager
            val display = windowManager.defaultDisplay
            val lp = dialogWindow.attributes
            lp.width = (display.width * 0.8).toInt() //设置宽度
            //        lp.height = (int) (lp.width * 1.23); //设置高度
            dialogWindow.attributes = lp

            return dialog
        }
    }

}