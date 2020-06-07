package com.acoria.unittimer.unittimer_api

//import org.mockito.ArgumentMatchers.any
import com.acoria.unittimer.unittimer_api.timer.countDownTimer.ICountDownTimer
import com.acoria.unittimer.unittimer_api.timer.unitTimer.IUnitTimer
import com.acoria.unittimer.unittimer_api.timer.unitTimer.IUnitTimerObserver
import com.acoria.unittimer.unittimer_api.timer.unitTimer.UnitTimer
import com.acoria.unittimer.unittimer_api.units.IUnit
import com.acoria.unittimer.unittimer_api.units.IUnitProvider
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito

class UnitTimerTest {

    private lateinit var unitTimer: IUnitTimer
    private lateinit var unitProvider: IUnitProvider
    private lateinit var observer: IUnitTimerObserver
    private lateinit var unit: IUnit

    @Before
    fun setup() {
        unitProvider = Mockito.mock(IUnitProvider::class.java)
        unit = Mockito.mock(IUnit::class.java)
        Mockito.`when`(unitProvider.getSuccessor()).thenReturn(unit)

        unitTimer = UnitTimer(unitProvider, 1)

        observer = Mockito.mock(IUnitTimerObserver::class.java)
        unitTimer.registerObserver(observer)

        Mockito.`when`(unit.getLength()).thenReturn(10)
    }

    @Test
    fun initializeFromNewUnit() {
        val newUnit = Mockito.mock(IUnit::class.java)
        Mockito.`when`(newUnit.getLength()).thenReturn(5)
        unitTimer.initializeFromUnit(newUnit, null)

        Mockito.verify(unitProvider, Mockito.times(1)).resetByUnit(newUnit)
        Mockito.verify(observer, Mockito.times(1)).onTimerInitialized(newUnit, 5)
    }

    @Test
    fun initializeFromNewUnitWithLength() {
        val newUnit = Mockito.mock(IUnit::class.java)
        Mockito.`when`(newUnit.getLength()).thenReturn(5)
        unitTimer.initializeFromUnit(newUnit, 8)

        Mockito.verify(unitProvider, Mockito.times(1)).resetByUnit(newUnit)
        Mockito.verify(observer, Mockito.times(1)).onTimerInitialized(newUnit, 8)
    }

    @Test
    fun skipUnitSuccessor() {
        val successorUnit = Mockito.mock(IUnit::class.java)
        Mockito.`when`(unitProvider.hasSuccessor()).thenReturn(true)
        Mockito.`when`(unitProvider.getSuccessor()).thenReturn(successorUnit)
        Mockito.`when`(successorUnit.getLength()).thenReturn(5)

        unitTimer.skipUnit()

        Mockito.verify(observer, Mockito.times(1)).onTimerInitialized(successorUnit, 5)
        Mockito.verify(observer, Mockito.times(1)).onTimerStopped(ICountDownTimer.TimerState.STOPPED)
    }

    @Test
    fun skipUnitNoSuccessor() {
        Mockito.`when`(unitProvider.hasSuccessor()).thenReturn(false)

        unitTimer.skipUnit()

        Mockito.verify(observer, Mockito.times(0)).onTimerInitialized(any(), anyInt())
        Mockito.verify(observer, Mockito.times(1)).onTimerStopped(ICountDownTimer.TimerState.STOPPED)
    }

    @Test
    fun backUnitPredecessor() {
        val predecessorUnit = Mockito.mock(IUnit::class.java)
        Mockito.`when`(unitProvider.hasPredecessor()).thenReturn(true)
        Mockito.`when`(unitProvider.getPredecessor()).thenReturn(predecessorUnit)
        Mockito.`when`(predecessorUnit.getLength()).thenReturn(5)

        unitTimer.backOneUnit()

        Mockito.verify(observer, Mockito.times(1)).onTimerInitialized(predecessorUnit, 5)
        Mockito.verify(observer, Mockito.times(1)).onTimerStopped(ICountDownTimer.TimerState.STOPPED)
    }

    @Test
    fun backUnitNoPredecessor() {
        Mockito.`when`(unitProvider.hasPredecessor()).thenReturn(false)

        unitTimer.backOneUnit()

        Mockito.verify(observer, Mockito.times(0)).onTimerInitialized(any(), anyInt())
    }

    @Test
    fun startTimer() {
        unitTimer.startTimer()
        Mockito.verify(observer, Mockito.times(1)).onTimerStarted(ICountDownTimer.TimerState.RUNNING)
    }

    @Test
    fun resumeTimer() {
        unitTimer.resumeTimer()
        Mockito.verify(observer, Mockito.times(1)).onTimerResumed(ICountDownTimer.TimerState.RUNNING)
    }

    @Test
    fun pauseTimer() {
        unitTimer.pauseTimer()
        Mockito.verify(observer, Mockito.times(1)).onTimerPaused(ICountDownTimer.TimerState.PAUSED)
    }

    @Test
    fun stopTimer() {
        unitTimer.stopTimer()
        Mockito.verify(observer, Mockito.times(1)).onTimerStopped(ICountDownTimer.TimerState.STOPPED)
    }

}