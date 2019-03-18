package me.khol.investor

import android.content.Context
import android.os.Bundle
import com.google.android.play.core.splitcompat.SplitCompat
import me.khol.base.base.BaseActivity

class InvestorActivity : BaseActivity() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_investor)
    }
}