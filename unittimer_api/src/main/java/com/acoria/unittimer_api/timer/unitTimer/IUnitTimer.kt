package com.acoria.unittimer.unittimer_api.timer.unitTimer

import com.acoria.unittimer.unittimer_api.timer.countDownTimer.ICountDownTimer
import com.acoria.unittimer.unittimer_api.units.IUnit

interface IUnitTimer : ICountDownTimer, IUnitTimerObservable {
    fun initializeFromUnit(unit: IUnit, startingTime: Int?)
    fun skipUnit()
    fun backOneUnit()
}