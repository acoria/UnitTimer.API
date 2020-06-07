package com.acoria.unittimer.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acoria.unittimer.unittimer_api.timer.unitTimer.IUnitTimerObserver
import com.acoria.unittimer.unittimer_api.timer.countDownTimer.ICountDownTimer
import com.acoria.unittimer.unittimer_api.timer.unitTimer.UnitTimer
import com.acoria.unittimer.unittimer_api.units.ExerciseDetails
import com.acoria.unittimer.unittimer_api.units.IUnit
import kotlinx.coroutines.launch

class IntegrationTestViewModel : ViewModel(), IUnitTimerObserver {

    private val currentResult = IntegrationTestResult(null, 0, ICountDownTimer.TimerState.INITIAL)
    private var currentViewState = IntegrationTestViewState()
        set(value) {
            field = value
            _liveDataViewState.value = value
        }
    private val unitTimer: UnitTimer
    private var _liveDataViewState = MutableLiveData<IntegrationTestViewState>()
    val liveDataViewState: LiveData<IntegrationTestViewState>
        get() = _liveDataViewState

    init {
        val exerciseDetails = listOf(
                ExerciseDetails("Stretch the Face"),
                ExerciseDetails("Stretch the Arms"),
                ExerciseDetails("Stretch the Back")
        )
        unitTimer = UnitTimer(UnitProviderStretches(exerciseDetails), 1)
        unitTimer.registerObserver(this)
    }

    fun backOneUnit() {
        unitTimer.backOneUnit()
    }

    fun toggleState() {
        unitTimer.toggleState()
    }

    fun skipUnit() {
        unitTimer.skipUnit()
    }

    override fun onNextImpulse(currentUnit: IUnit, remainingTime: Int) {
        viewModelScope.launch {
            currentResult.unit = currentUnit
            currentResult.remainingTime = remainingTime
            resultToViewState(Lce.Content(currentResult))
        }
    }

    override fun onTimerInitialized(currentUnit: IUnit, unitLength: Int) {
        viewModelScope.launch {
            currentResult.unit = currentUnit
            currentResult.remainingTime = unitLength
            resultToViewState(Lce.Content(currentResult))
        }
    }

    override fun onNextImpulse(remainingTime: Int) {
        viewModelScope.launch {
            currentResult.remainingTime = remainingTime
            resultToViewState(Lce.Content(currentResult))
        }
    }

    override fun onTimerPaused(timerState: ICountDownTimer.TimerState) {
        viewModelScope.launch {
            currentResult.timerState = timerState
            resultToViewState(Lce.Content(currentResult))
        }
    }

    override fun onTimerResumed(timerState: ICountDownTimer.TimerState) {
        viewModelScope.launch {
            currentResult.timerState = timerState
            resultToViewState(Lce.Content(currentResult))
        }
    }

    override fun onTimerStarted(timerState: ICountDownTimer.TimerState) {
        viewModelScope.launch {
            currentResult.timerState = timerState
            resultToViewState(Lce.Content(currentResult))
        }
    }

    override fun onTimerStopped(timerState: ICountDownTimer.TimerState) {
        viewModelScope.launch {
            currentResult.timerState = timerState
            resultToViewState(Lce.Content(currentResult))
        }
    }

    private fun resultToViewState(lceResult: Lce<IntegrationTestResult>) {
        currentViewState = when (lceResult) {
            is Lce.Content -> {
                val result = lceResult.packet
                val title = when (result.timerState) {
                    ICountDownTimer.TimerState.STOPPED -> "Stopped"
                    ICountDownTimer.TimerState.PAUSED -> "Paused"
                    else -> result.unit?.getTitle() ?: "error"
                }
                currentViewState.copy(
                        exerciseName = title,
                        remainingTime = result.remainingTime.toString()
                )
            }
            else -> {
                //TODO
                IntegrationTestViewState()
            }
        }
    }

}