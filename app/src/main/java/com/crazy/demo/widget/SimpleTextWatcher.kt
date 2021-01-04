package com.crazy.demo.widget

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by wtc on 2019/8/30
 */
open class SimpleTextWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {}
}
