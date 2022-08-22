package com.won983212.mongle.presentation.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.core.text.parseAsHtml
import androidx.core.text.toHtml
import androidx.core.text.toSpanned

class TextResource {
    val text: String
    val resId: Int?
    val args: Array<out Any?>?

    constructor(text: String?) {
        this.text = text ?: ""
        this.resId = null
        this.args = null
    }

    constructor(
        @StringRes resId: Int? = null,
        vararg args: Any?
    ) {
        this.text = ""
        this.resId = resId
        this.args = args
    }

    override fun toString(): String {
        if (text.isNotBlank()) {
            return "StringResourceWithArg[text=${text}]"
        } else if (args != null) {
            return "StringResourceWithArg[resId=${resId}, args=${args.joinToString()}]"
        } else {
            return "StringResourceWithArg[resId=${resId}]"
        }
    }

    fun toCharSequence(context: Context): CharSequence {
        if (resId == null) {
            return text
        }

        val parsedArgs: Array<Any?> =
            if (args == null) {
                arrayOf()
            } else {
                arrayOf(*args)
            }

        for (i in parsedArgs.indices) {
            val arg = parsedArgs[i]
            if (arg is TextResource) {
                parsedArgs[i] = arg.toCharSequence(context)
            }
        }

        return context.getText(resId)
            .toSpanned()
            .toHtml()
            .format(*parsedArgs)
            .parseAsHtml()
            .trim()
    }
}