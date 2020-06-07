package com.acoria.unittimer.unittimer_api

import com.acoria.unittimer.unittimer_api.timer.countDownTimer.CountDownTimer
import com.acoria.unittimer.unittimer_api.timer.countDownTimer.ICountDownTimer
import com.acoria.unittimer.unittimer_api.timer.countDownTimer.ICountDownTimerObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class CountDownTimerTest {

    private lateinit var countDownTimer: ICountDownTimer
    private lateinit var observer: ICountDownTimerObserver
    private lateinit var scheduledExecutorService: ScheduledExecutorService
    private var scheduledTask: ScheduledFuture<*>? = null

    @Before
    fun setup() {
        scheduledExecutorService = Mockito.mock(ScheduledExecutorService::class.java)
        scheduledTask = Mockito.mock(ScheduledFuture::class.java)
        Mockito.`when`(scheduledExecutorService.scheduleAtFixedRate(any(), anyLong(), anyLong(), any())).thenReturn(scheduledTask)

        observer = Mockito.mock(ICountDownTimerObserver::class.java)
        val timer = CountDownTimer(1, scheduledExecutorService)
        timer.registerObserver(observer)
        countDownTimer = timer
    }

    @Test
    fun initialTimerState() {
        Assert.assertEquals(ICountDownTimer.TimerState.INITIAL, countDownTimer.getState())
    }

    @Test
    fun startTimer() {
        countDownTimer.startTimer()

        Assert.assertEquals(ICountDownTimer.TimerState.RUNNING, countDownTimer.getState())
        Mockito.verify(observer, Mockito.times(1)).onTimerStarted(ICountDownTimer.TimerState.RUNNING)
        Mockito.verify(scheduledExecutorService, Mockito.times(1)).scheduleAtFixedRate(countDownTimer, 1, 1, TimeUnit.SECONDS)
    }

    @Test
    fun toggleStateFromInitial() {
        countDownTimer.toggleState()

        Mockito.verify(observer, Mockito.times(1)).onTimerStarted(ICountDownTimer.TimerState.RUNNING)
        Assert.assertEquals(ICountDownTimer.TimerState.RUNNING, countDownTimer.getState())
    }

    @Test
    fun toggleStateFromStopped() {
        countDownTimer.stopTimer()
        countDownTimer.toggleState()

        Mockito.verify(observer, Mockito.times(1)).onTimerStarted(ICountDownTimer.TimerState.RUNNING)
        Assert.assertEquals(ICountDownTimer.TimerState.RUNNING, countDownTimer.getState())
    }

    @Test
    fun toggleStateFromPaused() {
        countDownTimer.pauseTimer()
        countDownTimer.toggleState()

        Mockito.verify(observer, Mockito.times(1)).onTimerResumed(ICountDownTimer.TimerState.RUNNING)
        Assert.assertEquals(ICountDownTimer.TimerState.RUNNING, countDownTimer.getState())
    }

    @Test
    fun toggleStateFromRunning() {
        countDownTimer.startTimer()
        countDownTimer.toggleState()

        Mockito.verify(observer, Mockito.times(1)).onTimerPaused(ICountDownTimer.TimerState.PAUSED)
        Assert.assertEquals(ICountDownTimer.TimerState.PAUSED, countDownTimer.getState())
    }

    @Test
    fun removeObserver() {
        countDownTimer.removeObserver(observer)
        countDownTimer.startTimer()

        Mockito.verify(observer, Mockito.times(0)).onTimerStarted(ICountDownTimer.TimerState.RUNNING)
    }
}