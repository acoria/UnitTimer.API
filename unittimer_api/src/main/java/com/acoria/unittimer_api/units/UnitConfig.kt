package com.acoria.unittimer.unittimer_api.units

import kotlin.collections.HashMap

class UnitConfig : IUnitConfig {

    private val lengthList: MutableMap<IUnitConfig.UnitLengthType, Int> = HashMap()

    override fun getLengthByType(type: IUnitConfig.UnitLengthType): Int {
        return lengthList[type]!!
    }

    protected fun addUnitLength(type: IUnitConfig.UnitLengthType, length: Int) {
        //multiply with 1000 since timer accepts only milliseconds
        lengthList[type] = length * 1000
    }
}