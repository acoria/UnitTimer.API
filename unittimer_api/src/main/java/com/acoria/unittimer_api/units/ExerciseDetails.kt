package com.acoria.unittimer.unittimer_api.units

class ExerciseDetails(private val exerciseName: String) : IExerciseDetails {
    override fun getImageResource(): Int {
        return -1
    }

    override fun getName(): String? {
        return exerciseName
    }

    override fun isOneSided(): Boolean {
        return false
    }
}