package com.acoria.unittimer.unittimer_api.timer.countDownTimer

interface ICountDownTimerObserver{

    fun onNextImpulse(remainingTime: Int)
    fun onTimerStarted(timerState: ICountDownTimer.TimerState)
    fun onTimerResumed(timerState: ICountDownTimer.TimerState)
    fun onTimerPaused(timerState: ICountDownTimer.TimerState)
    fun onTimerStopped(timerState: ICountDownTimer.TimerState)

}