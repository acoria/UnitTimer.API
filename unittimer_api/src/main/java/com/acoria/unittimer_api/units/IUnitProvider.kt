package com.acoria.unittimer.unittimer_api.units

interface IUnitProvider {
    fun getFirst(): IUnit
    fun getLast(): IUnit
    fun getUnitById(id: String): IUnit?
    fun resetByUnit(unit: IUnit)
    fun reset()
    fun getPredecessor(): IUnit?
    fun getSuccessor(): IUnit?
    fun hasPredecessor(): Boolean
    fun hasSuccessor(): Boolean
    fun getCurrentExercisePosition(): Int
    fun getCurrentUnit(): IUnit?
    fun getNumberOfExercises(): Int
    fun getUnits(): List<IUnit>
    fun getUnitNames(): List<IExerciseDetails>
    fun getTotalLength(): Int
    fun getSneakSuccessor(): IUnit?
    fun getSneakPredecessor(): IUnit?
}