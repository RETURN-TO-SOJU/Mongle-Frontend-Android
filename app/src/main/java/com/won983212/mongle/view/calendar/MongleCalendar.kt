package com.won983212.mongle.view.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.widget.NestedScrollView
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.won983212.mongle.R
import com.won983212.mongle.databinding.CalendarMongleBinding
import com.won983212.mongle.util.dpToPx
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

class MongleCalendar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy. MM")
    private val binding = CalendarMongleBinding.inflate(LayoutInflater.from(context))

    var selectedDate: LocalDate? = null
        private set

    init {
        val currentMonth = YearMonth.now()
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        val daysOfWeek = resources.getStringArray(R.array.calendar_weekdays)

        binding.calendar.apply {
            daySize = CalendarView.sizeAutoWidth(dpToPx(context, 44))
            dayBinder = MongleDayBinder(this@MongleCalendar, this@MongleCalendar::selectDate)
            monthHeaderBinder = MongleMonthWeekHeaderBinder(daysOfWeek)
            monthScrollListener = {
                binding.textCalendarMonth.text = yearMonthFormatter.format(it.yearMonth)
            }
            isNestedScrollingEnabled = false
            setupAsync(
                currentMonth.minusMonths(5),
                currentMonth.plusMonths(5),
                firstDayOfWeek
            ) {
                this.scrollToMonth(currentMonth)
            }
        }

        initEvents()
        addView(binding.root)
    }

    private fun initEvents() {
        binding.btnCalendarPrevMonth.setOnClickListener {
            binding.calendar.apply {
                findFirstVisibleMonth()?.let {
                    smoothScrollToMonth(it.yearMonth.previous)
                }
            }
        }

        binding.btnCalendarNextMonth.setOnClickListener {
            binding.calendar.apply {
                findFirstVisibleMonth()?.let {
                    smoothScrollToMonth(it.yearMonth.next)
                }
            }
        }
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date

            oldDate?.let { binding.calendar.notifyDateChanged(it) }
            binding.calendar.notifyDateChanged(date)
        }
    }

    // scroll view가 작동하지 않는 버그 수정
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        performClick()

        var parentView = parent
        while (parentView != null) {
            if (parentView is NestedScrollView) {
                parentView.requestDisallowInterceptTouchEvent(true)
                break
            }
            parentView = parentView.parent
        }

        return false
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}