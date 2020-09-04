package com.meuus90.daumbooksearch.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.meuus90.daumbooksearch.R

object Caller {
    internal fun openMainActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    internal fun openUrlLink(context: Context, url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(
            Intent.createChooser(
                browserIntent,
                context.getString(R.string.next_choose_browser)
            )
        )
    }
}