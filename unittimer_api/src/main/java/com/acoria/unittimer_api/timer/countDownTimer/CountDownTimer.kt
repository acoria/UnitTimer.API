package com.acoria.unittimer.unittimer_api.timer.countDownTimer

import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class CountDownTimer(
        private val intervalInSeconds: Int,
        private val scheduledExecutorService: ScheduledExecutorService = Executors.newScheduledThreadPool(1)) : ICountDownTimer {

    private var scheduledTask: ScheduledFuture<*>? = null
    private val timerObservers = mutableListOf<ICountDownTimerObserver>()
    private var timerState = ICountDownTimer.TimerState.INITIAL
    private var remainingTime: Int = 0
    private val SYNC_MONITOR = Object()

    override fun startTimer() {
        scheduleTask()
        timerStarted()
    }

    override fun resumeTimer() {
        scheduleTask()
        timerResumed()
    }

    override fun pauseTimer() {
        cancelScheduledTask()
        timerPaused()
    }

    override fun stopTimer() {
        cancelScheduledTask()
        timerStopped()
    }

    override fun getState(): ICountDownTimer.TimerState {
        return timerState
    }

    override fun toggleState() {
        when (timerState) {
            ICountDownTimer.TimerState.INITIAL, ICountDownTimer.TimerState.STOPPED -> startTimer()
            ICountDownTimer.TimerState.PAUSED -> resumeTimer()
            ICountDownTimer.TimerState.RUNNING -> pauseTimer()
        }
    }

    override fun initializeWithNewLength(length: Int) {
        remainingTime = length
    }

    override fun getFormattedTime(time: Int): String? {
        val minutes = time / 60.toLong()
        return when (val seconds = time % 60.toLong()) {
            0L -> "$minutes:00"
            in 1..9 -> "$minutes:0$seconds"
            else -> "$minutes:$seconds"
        }
    }

    override fun run() {
        synchronized(SYNC_MONITOR) {
            //Synchronization of access to non-thread-safe parts of code,
            // see: https://www.techyourchance.com/thread-safe-observer-design-pattern-in-java/
            try {
                nextImpulse()
            } catch (e: Exception) {
                Log.e(this::class.java.simpleName, e.message ?: "error")
            }
        }
    }

    override fun nextImpulse() {
        if (remainingTime > 0) {
            remainingTime--
        } else {
            stopTimer()
        }
        for(observer in timerObservers){
            observer.onNextImpulse(remainingTime)
        }
    }

    override fun timerStarted() {
        timerState = ICountDownTimer.TimerState.RUNNING
        for(observer in timerObservers){
            observer.onTimerStarted(timerState)
        }
    }

    override fun timerStopped() {
        timerState = ICountDownTimer.TimerState.STOPPED
        for(observer in timerObservers){
            observer.onTimerStopped(timerState)
        }
    }

    override fun timerResumed() {
        timerState = ICountDownTimer.TimerState.RUNNING
        for(observer in timerObservers){
            observer.onTimerResumed(timerState)
        }
    }

    override fun timerPaused() {
        timerState = ICountDownTimer.TimerState.PAUSED
        for(observer in timerObservers){
            observer.onTimerPaused(timerState)
        }
    }

    override fun registerObserver(countDownObserver: ICountDownTimerObserver) {
        timerObservers.add(countDownObserver)
    }

    override fun removeObserver(countDownObserver: ICountDownTimerObserver) {
        timerObservers.remove(countDownObserver)
    }

    private fun scheduleTask() {
        scheduledTask = scheduledExecutorService.scheduleAtFixedRate(this, intervalInSeconds.toLong(), intervalInSeconds.toLong(), TimeUnit.SECONDS)
    }

    private fun cancelScheduledTask() {
        if (scheduledTask != null) {
            scheduledTask?.cancel(true)
        }
    }
}