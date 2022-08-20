package com.won983212.mongle.view.overview

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.won983212.mongle.R
import com.won983212.mongle.common.base.BaseDataActivity
import com.won983212.mongle.databinding.ActivityOverviewBinding
import com.won983212.mongle.view.tutorial.TutorialActivity

class OverviewActivity : BaseDataActivity<ActivityOverviewBinding>() {
    private val viewModel by viewModels<OverviewViewModel>()

    override val layoutId: Int = R.layout.activity_overview

    override fun onInitialize() {
        binding.viewModel = viewModel
        setSupportActionBar(binding.toolbarOverview)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel.calendarEmotions.observe(this) {
            binding.calendarOverview.setDayEmotions(it)
        }

        binding.btnOverviewTutorialKakaoExport.setOnClickListener {
            TutorialActivity.startKakaoTutorial(this)
        }

        initKeywordList()
    }

    private fun initKeywordList() {
        binding.listKeyword.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = KeywordAdapter()
        }
    }
}