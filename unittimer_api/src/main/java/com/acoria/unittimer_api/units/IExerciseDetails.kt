package com.acoria.unittimer.unittimer_api.units

interface IExerciseDetails {
    fun getName(): String?
    fun isOneSided(): Boolean
    fun getImageResource(): Int
}