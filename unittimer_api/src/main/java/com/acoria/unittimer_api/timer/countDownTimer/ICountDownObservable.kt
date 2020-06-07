package com.acoria.unittimer.unittimer_api.timer.countDownTimer

interface ICountDownTimerObservable{

    fun registerObserver(countDownObserver: ICountDownTimerObserver)
    fun removeObserver(countDownObserver: ICountDownTimerObserver)

    fun nextImpulse()
    fun timerStarted()
    fun timerResumed()
    fun timerPaused()
    fun timerStopped()

}