package com.acoria.unittimer.unittimer_api.timer.countDownTimer

interface ICountDownTimer: Runnable, ICountDownTimerObservable {

    enum class TimerState{ INITIAL, RUNNING, STOPPED, PAUSED }

    fun startTimer()
    fun resumeTimer()
    fun pauseTimer()
    fun stopTimer()
    fun getState() : TimerState
    fun toggleState()
    fun initializeWithNewLength(length: Int)
    fun getFormattedTime(time: Int): String?

}