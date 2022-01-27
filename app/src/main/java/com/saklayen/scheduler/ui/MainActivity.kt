package com.saklayen.scheduler.ui

import android.os.Bundle
import com.saklayen.scheduler.R
import com.saklayen.scheduler.base.ui.BaseActivity
import com.saklayen.scheduler.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}