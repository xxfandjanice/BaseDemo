package com.crazy.demo.widget.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.crazy.demo.R
import com.crazy.demo.entity.BaseBottomBean
import com.crazy.kotlin_mvvm.ext.layoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Created by wtc on 2019/11/25
 */
class BottomListDialog<T>(
    context: Context,
    val list: List<BaseBottomBean<T>>,
    val itemClickListener: (selectedItem: T?) -> Unit
) {
    private var bottomDialog: BottomSheetDialog = BottomSheetDialog(context)

    init {
        val view = context.layoutInflater?.inflate(R.layout.dialog_bottom_list, null)
        view?.run {
            val rv = findViewById<RecyclerView>(R.id.rv)
            findViewById<View>(R.id.tv_cancel).setOnClickListener { bottomDialog.dismiss() }

            rv.layoutManager = LinearLayoutManager(context)
            val adapter = object : BaseQuickAdapter<BaseBottomBean<T>, BaseViewHolder>(
                R.layout.item_dialog_bottom_list,
                list
            ) {
                override fun convert(helper: BaseViewHolder, item: BaseBottomBean<T>?) {
                    helper.setText(R.id.tv, item?.name)
                    helper.setTextColor(
                        R.id.tv,
                        if (item!!.isSelected) context.resources.getColor(R.color.theme) else context.resources.getColor(
                            R.color.bg_color
                        )
                    )
                    helper.setTypeface(
                        R.id.tv,
                        if (item.isSelected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                    )
                }
            }
            rv.adapter = adapter
            adapter.setOnItemClickListener { _, _, position ->
                run {
                    bottomDialog.dismiss()
                    itemClickListener(list[position].data)
                }
            }
            bottomDialog.setContentView(this)
        }

        bottomDialog.window?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bottomDialog.show()
    }

    fun show() = bottomDialog.show()

    fun dismiss() = bottomDialog.dismiss()

}