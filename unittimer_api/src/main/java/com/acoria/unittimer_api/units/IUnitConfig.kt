package com.acoria.unittimer.unittimer_api.units

interface IUnitConfig {

    enum class UnitLengthType {
        UNIT_LENGTH_SHORT,
        UNIT_LENGTH_MIDDLE,
        UNIT_LENGTH_LONG,
        UNIT_LENGTH_PREP,
        UNIT_LENGTH_SWAP_SIDE,
        UNIT_LENGTH_BREAK
    }

    fun getLengthByType(type: UnitLengthType): Int

}