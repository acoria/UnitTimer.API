package com.acoria.unittimer.api

import com.acoria.unittimer.unittimer_api.units.IExerciseDetails
import com.acoria.unittimer.unittimer_api.units.MainUnit
import com.acoria.unittimer.unittimer_api.units.UnitProvider

class UnitProviderStretches(exerciseDetails: List<IExerciseDetails>) : UnitProvider(2, exerciseDetails){

    override fun initialize() {
        val exerciseDetails = getUnitNames()

        for(rep in 1 until getReps())
        for(details in exerciseDetails){
            addUnit(MainUnit.Exercise(details.getName() ?: "Exercise", 2), rep)
        }
    }
}