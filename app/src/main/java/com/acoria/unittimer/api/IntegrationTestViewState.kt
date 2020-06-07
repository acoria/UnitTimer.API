package com.acoria.unittimer.api

import com.acoria.unittimer.unittimer_api.timer.countDownTimer.ICountDownTimer
import com.acoria.unittimer.unittimer_api.units.IUnit

data class IntegrationTestViewState(
        val exerciseName: String = "",
        val remainingTime: String = ""
)

data class IntegrationTestResult(
        var unit: IUnit? = null,
        var remainingTime: Int,
        var timerState: ICountDownTimer.TimerState
)