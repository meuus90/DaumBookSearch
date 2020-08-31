package com.meuus90.daumbooksearch.presentation

import android.content.Context
import android.content.Intent
import com.meuus90.daumbooksearch.presentation.book.MainActivity

object Caller {
    internal fun logoutApp(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}