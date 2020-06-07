package com.acoria.unittimer.unittimer_api.timer.unitTimer

import com.acoria.unittimer.unittimer_api.timer.countDownTimer.CountDownTimer
import com.acoria.unittimer.unittimer_api.timer.countDownTimer.ICountDownTimer
import com.acoria.unittimer.unittimer_api.timer.countDownTimer.ICountDownTimerObserver
import com.acoria.unittimer.unittimer_api.units.IUnit
import com.acoria.unittimer.unittimer_api.units.IUnitProvider
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class UnitTimer(
        private val unitProvider: IUnitProvider,
        intervalInSeconds: Int,
        scheduledExecutorService: ScheduledExecutorService = Executors.newScheduledThreadPool(1),
        private val countDownTimer: ICountDownTimer = CountDownTimer(intervalInSeconds, scheduledExecutorService))
    : IUnitTimer, ICountDownTimerObserver, ICountDownTimer by countDownTimer {

    private lateinit var currentUnit: IUnit
    private val unitTimerObservers = mutableListOf<IUnitTimerObserver>()

    init {
        countDownTimer.registerObserver(this)
        initialize()
    }


    private fun initialize() {
        val firstUnit = unitProvider.getSuccessor() ?: return
        initializeFromUnit(firstUnit, null)
    }

    override fun initializeFromUnit(unit: IUnit, startingTime: Int?) {
        unitProvider.resetByUnit(unit)
        val length = startingTime ?: unit.getLength()
        setNewUnit(unit, length)
        timerInitialized(length)
    }

    private fun setNewUnit(unit: IUnit, startingTime: Int?) {
        currentUnit = unit
        val length = startingTime ?: unit.getLength()
        countDownTimer.initializeWithNewLength(length)
    }

    override fun skipUnit() {
        countDownTimer.stopTimer()
        if (unitProvider.hasSuccessor()) {
            //while there is another exercise
            initializeFromUnit(unitProvider.getSuccessor()!!, null)
        }
    }

    override fun backOneUnit() {
        countDownTimer.stopTimer()
        if (unitProvider.hasPredecessor()) {
            initializeFromUnit(unitProvider.getPredecessor()!!, null)
        }
    }

    override fun registerObserver(observer: IUnitTimerObserver) {
        unitTimerObservers.add(observer)
    }

    override fun removeObserver(observer: IUnitTimerObserver) {
        unitTimerObservers.remove(observer)
    }

    private fun timerInitialized(startingTime: Int) {
        for (observer in unitTimerObservers) {
            observer.onTimerInitialized(currentUnit, startingTime)
        }
    }

    override fun onNextImpulse(remainingTime: Int) {

        val unitForThisImpulse = currentUnit

        if (remainingTime == 0) {
            //while there is another unit
            if (unitProvider.hasSuccessor()) {
                //get next exercise
                setNewUnit(unitProvider.getSuccessor()!!, null)
            }
        }
        for (observer in unitTimerObservers) {
            observer.onNextImpulse(unitForThisImpulse, remainingTime)
        }

    }

    override fun onTimerStarted(timerState: ICountDownTimer.TimerState) {
        for (observer in unitTimerObservers) {
            observer.onTimerStarted(countDownTimer.getState())
        }
    }

    override fun onTimerResumed(timerState: ICountDownTimer.TimerState) {
        for (observer in unitTimerObservers) {
            observer.onTimerResumed(countDownTimer.getState())
        }
    }

    override fun onTimerPaused(timerState: ICountDownTimer.TimerState) {
        for (observer in unitTimerObservers) {
            observer.onTimerPaused(countDownTimer.getState())
        }
    }

    override fun onTimerStopped(timerState: ICountDownTimer.TimerState) {
        for (observer in unitTimerObservers) {
            observer.onTimerStopped(countDownTimer.getState())
        }
    }
}