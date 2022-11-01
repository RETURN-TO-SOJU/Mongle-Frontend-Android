package com.rtsoju.mongle.presentation.view.overview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rtsoju.mongle.databinding.FragmentOverviewBinding
import com.rtsoju.mongle.domain.model.Emotion
import com.rtsoju.mongle.presentation.common.calendar.MongleCalendar
import com.rtsoju.mongle.presentation.util.getSerializableExtraCompat
import com.rtsoju.mongle.presentation.view.daydetail.DayDetailActivity
import com.rtsoju.mongle.presentation.view.tutorial.TutorialActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private val viewModel by viewModels<OverviewViewModel>()
    private lateinit var dayDetailResult: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOverviewBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val today = LocalDate.now()

        dayDetailResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                callbackDayDetailResult(binding.calendarOverview, it?.data)
            }

        viewModel.attachDefaultErrorHandler(requireActivity())
        viewModel.apply {
            eventCalendarDataLoaded.observe(viewLifecycleOwner) {
                binding.calendarOverview.addDayEmotions(it)
            }
        }

        binding.calendarOverview.apply {
            setOnSelectedListener { date ->
                viewModel.setSelectedDate(date)
            }
            setOnClickSelectedListener { date ->
                openDayDetail(date)
            }
            setOnInitializedListener {
                selectDate(today)
            }
            setOnMonthLoadedListener { from, to ->
                viewModel.loadCalendarData(from, to)
            }
        }

        binding.btnOverviewTutorialKakaoExport.setOnClickListener {
            context?.let { it1 -> TutorialActivity.startKakaoTutorial(it1) }
        }

        binding.layoutOverviewSummaryCard.setOnClickListener {
            binding.calendarOverview.selectedDate?.let {
                openDayDetail(it)
            }
        }

        binding.listKeyword.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = KeywordAdapter()
        }

        return binding.root
    }

    private fun openDayDetail(date: LocalDate) {
        Intent(context, DayDetailActivity::class.java).apply {
            putExtra(DayDetailActivity.EXTRA_DATE, date)
            dayDetailResult.launch(this)
        }
    }

    /**
     * DayDetail에서 Emotion을 수정했으면, 이를 calendar와 local db에 반영해야 한다.
     * 이를 수행하는 callback
     */
    private fun callbackDayDetailResult(
        calendar: MongleCalendar,
        data: Intent?
    ) {
        val selected = data?.getSerializableExtraCompat(
            DayDetailActivity.RESULT_SELECTED_DATE
        ) ?: calendar.selectedDate

        if (selected != null) {
            viewModel.setSelectedDate(selected)
            calendar.selectDate(selected)

            val changedEmotion: Emotion? =
                data?.getSerializableExtraCompat(DayDetailActivity.RESULT_CHANGED_EMOTION)

            if (changedEmotion != null) {
                viewModel.updateEmotion(selected, changedEmotion)
                calendar.addDayEmotions(
                    mapOf(selected to changedEmotion)
                )
            }
        }
    }
}