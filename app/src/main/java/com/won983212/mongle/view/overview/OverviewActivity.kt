package com.won983212.mongle.view.overview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.won983212.mongle.databinding.ActivityOverviewBinding

class OverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOverviewBinding
    private val keywords: List<String> = mutableListOf("학교", "디자인", "자바") // TODO mocking data
    private val keywordAdapter = KeywordListAdapter(keywords)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOverviewBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        initKeywordList()
    }

    private fun initKeywordList() {
        binding.listKeyword.apply {
            val linearLayoutManager = LinearLayoutManager(this@OverviewActivity)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = linearLayoutManager
            adapter = keywordAdapter
        }
    }
}