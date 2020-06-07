package com.acoria.unittimer.unittimer_api.timer.unitTimer

import com.acoria.unittimer.unittimer_api.timer.countDownTimer.ICountDownTimerObservable

interface IUnitTimerObservable : ICountDownTimerObservable{
    fun registerObserver(observer: IUnitTimerObserver)
    fun removeObserver(observer: IUnitTimerObserver)

//    fun nextImpulse()
//    fun timerStarted()
//    fun timerResumed()
//    fun timerPaused()
//    fun timerStopped()
//    fun timerFinished()
//    fun timerInitialized()
}
