package com.acoria.unittimer.unittimer_api.units

import java.util.*
sealed class MainUnit (var unitTitle: String, var unitLength: Int): IUnit {

    class Break(title: String = "Break", length: Int): MainUnit(title, length)
    class Exercise(exerciseTitle: String, length: Int, isOneSided: Boolean = false) : MainUnit(exerciseTitle, length){
        init {
            super.isUnitOneSided = isOneSided
        }
    }

    private val unitId = UUID.randomUUID().toString()
    private var infoImage: Int? = 0
    protected var isUnitOneSided: Boolean = false

    override fun getId(): String {
        return unitId
    }

    override fun getTitle(): String {
        return unitTitle
    }

    override fun getLength(): Int {
        return unitLength
    }

    override fun getInfoImage(): Int? {
        return infoImage
    }

    override fun setTitle(title: String) {
        unitTitle = title
    }

    override fun setInfoImage(infoImage: Int?) {
        this.infoImage = infoImage
    }

    override fun isOneSided(): Boolean {
        return isUnitOneSided
    }
}
