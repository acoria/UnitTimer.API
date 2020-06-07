package com.acoria.unittimer.unittimer_api.timer.unitTimer

import com.acoria.unittimer.unittimer_api.timer.countDownTimer.ICountDownTimerObserver
import com.acoria.unittimer.unittimer_api.units.IUnit

interface IUnitTimerObserver : ICountDownTimerObserver {
    fun onNextImpulse(currentUnit: IUnit, remainingTime: Int)
    fun onTimerInitialized(currentUnit: IUnit, unitLength: Int)
}
