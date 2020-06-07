package com.acoria.unittimer.unittimer_api.units

abstract class UnitProvider(private val reps: Int, private val names: List<IExerciseDetails>) : IUnitProvider {

    private val units: MutableList<IUnit> = ArrayList()
    private var currentPosition = -1
    private var totalLength = 0

    init {
        initialize()
    }

    /**The initialize method fills the units list with units by calling "addUnit"**/
    abstract fun initialize()

    override fun resetByUnit(unit: IUnit) {
        currentPosition = units.indexOf(unit)
    }

    override fun reset() {
        currentPosition = -1
    }

    override fun getUnitNames(): List<IExerciseDetails> {
        return names
    }

    protected fun getReps(): Int {
        return reps
    }

    protected fun addUnitToStack(unit: IUnit) {
        units.add(unit)
        totalLength += unit.getLength()
    }

    override fun getFirst(): IUnit {
        return units[0]
    }

    override fun getLast(): IUnit {
        return units[units.size - 1]
    }

    override fun getUnitById(id: String): IUnit? {
        for (unit in units) {
            if (unit.getId() == id) {
                return unit
            }
        }
        return null
    }

    override fun getPredecessor(): IUnit? {
        return if (hasPredecessor()) {
            units[--currentPosition]
        } else null
    }

    override fun getSuccessor(): IUnit? {
        return if (hasSuccessor()) {
            units[++currentPosition]
        } else null
    }

    override fun getCurrentUnit(): IUnit? {
        return units[currentPosition]
    }

    override fun hasSuccessor(): Boolean {
        return currentPosition + 1 < units.size
    }

    override fun hasPredecessor(): Boolean {
        return currentPosition > 0
    }

    override fun getUnits(): List<IUnit> {
        return units
    }

    protected fun isLastExercise(currentRep: Int, numberOfExercises: Int, currentExercisePos: Int): Boolean {
        if (currentRep < getReps() - 1) {
            return false
        }
        return currentExercisePos >= numberOfExercises - 1
    }

    private fun isLastRep(rep: Int): Boolean {
        return rep == getReps()
    }

    private fun isNumberOfRepsEven(): Boolean {
        return getReps() % 2 == 0
    }

    protected fun addUnit(trainingUnit: IUnit, currentRep: Int) {
        if (needsSplit(trainingUnit, currentRep)) {
            addLeftSidedExercise(trainingUnit)
            addSideSwap()
            addRightSidedExercise(trainingUnit)
        } else {
            addUnitToStack(trainingUnit)
        }
    }

    private fun addSideSwap() {
        addUnitToStack(MainUnit.Break("Swap side", 2))
    }

    private fun needsSplit(trainingUnit: IUnit, currentRep: Int): Boolean {
        //exercise needs splitting for each side in last rep
        return trainingUnit.isOneSided() && isLastRep(currentRep) && !isNumberOfRepsEven()
    }

    private fun addLeftSidedExercise(sourceTrainingUnit: IUnit) {
        val newTrainingUnit: IUnit = MainUnit.Exercise(sourceTrainingUnit.getTitle() + " - left", sourceTrainingUnit.getLength() / 2, true)
        addExercise(sourceTrainingUnit, newTrainingUnit)
    }

    private fun addRightSidedExercise(sourceTrainingUnit: IUnit) {
        val newTrainingUnit: IUnit = MainUnit.Exercise(sourceTrainingUnit.getTitle() + " - right", sourceTrainingUnit.getLength() / 2, true)
        addExercise(sourceTrainingUnit, newTrainingUnit)
    }

    private fun addExercise(sourceTrainingUnit: IUnit, newTrainingUnit: IUnit) {
        newTrainingUnit.setInfoImage(sourceTrainingUnit.getInfoImage())
        addUnitToStack(newTrainingUnit)
    }

    override fun getTotalLength(): Int {
        return totalLength
    }

    override fun getSneakSuccessor(): IUnit? {
        return if (hasSuccessor()) {
            units[currentPosition + 1]
        } else null
    }

    override fun getSneakPredecessor(): IUnit? {
        return if (hasPredecessor()) {
            units[currentPosition - 1]
        } else null
    }

    override fun getCurrentExercisePosition(): Int {
        var currentExercisePosition = 0
        for (trainingUnit in units) {
            if (trainingUnit is MainUnit.Exercise) {
                currentExercisePosition += 1
                if (trainingUnit === units[currentPosition]) {
                    break
                }
            }
        }
        return currentExercisePosition
    }

    override fun getNumberOfExercises(): Int {
        var numberOfExercises = 0
        for (trainingUnit in units) {
            if(trainingUnit is MainUnit.Exercise) {
                numberOfExercises += 1
            }
        }
        return numberOfExercises
    }
}