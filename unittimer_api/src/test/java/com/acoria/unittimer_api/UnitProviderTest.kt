package com.acoria.unittimer.unittimer_api

import com.acoria.unittimer.unittimer_api.units.IExerciseDetails
import com.acoria.unittimer.unittimer_api.units.IUnit
import com.acoria.unittimer.unittimer_api.units.IUnitProvider
import com.acoria.unittimer.unittimer_api.units.UnitProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock


class UnitProviderTest {
    private lateinit var testUnitProvider: TestUnitProvider
    private lateinit var unitProvider: IUnitProvider
    private val firstId = "1"
    private val secondId = "2"
    private val thirdId = "3"

    @Before
    fun setup() {
        testUnitProvider = TestUnitProvider()
        unitProvider = testUnitProvider
        Mockito.`when`(testUnitProvider.firstTrainingUnit.getId()).thenReturn(firstId)
        Mockito.`when`(testUnitProvider.secondTrainingUnit.getId()).thenReturn(secondId)
        Mockito.`when`(testUnitProvider.thirdTrainingUnit.getId()).thenReturn(thirdId)
    }

    @Test
    fun getFirst() {
        assertEquals(testUnitProvider.firstTrainingUnit, unitProvider.getFirst())
    }


    @Test
    fun getLast() {
        assertEquals(testUnitProvider.thirdTrainingUnit, unitProvider.getLast())
    }

    @Test
    fun getUnitById() {
        assertEquals(testUnitProvider.firstTrainingUnit, unitProvider.getUnitById(testUnitProvider.firstTrainingUnit.getId()))
        assertEquals(testUnitProvider.secondTrainingUnit, unitProvider.getUnitById(testUnitProvider.secondTrainingUnit.getId()))
        assertEquals(testUnitProvider.thirdTrainingUnit, unitProvider.getUnitById(testUnitProvider.thirdTrainingUnit.getId()))
    }

    @Test
    fun getPredecessor() {
        unitProvider.getSuccessor()
        unitProvider.getSuccessor()
        unitProvider.getSuccessor()
        assertEquals(testUnitProvider.secondTrainingUnit, unitProvider.getPredecessor())
        assertEquals(testUnitProvider.firstTrainingUnit, unitProvider.getPredecessor())
    }

    @Test
    fun getSuccessor() {
        assertEquals(testUnitProvider.firstTrainingUnit, unitProvider.getSuccessor())
        assertEquals(testUnitProvider.secondTrainingUnit, unitProvider.getSuccessor())
        assertEquals(testUnitProvider.thirdTrainingUnit, unitProvider.getSuccessor())
    }

    @Test
    fun hasPredecessor() {
        unitProvider.getSuccessor()
        unitProvider.getSuccessor()
        assertTrue(unitProvider.hasPredecessor())
        unitProvider.getSuccessor()
        assertTrue(unitProvider.hasPredecessor())
    }

    @Test
    fun hasNoPredecessor() {
        assertFalse(unitProvider.hasPredecessor())
        unitProvider.getSuccessor()
        assertFalse(unitProvider.hasPredecessor())
    }

    @Test
    fun hasSuccessor() {
        assertTrue(unitProvider.hasSuccessor())
        unitProvider.getSuccessor()
        assertTrue(unitProvider.hasSuccessor())
        unitProvider.getSuccessor()
        assertTrue(unitProvider.hasSuccessor())
    }

    @Test
    fun hasNoSuccessor() {
        unitProvider.getSuccessor()
        unitProvider.getSuccessor()
        unitProvider.getSuccessor()
        assertFalse(unitProvider.hasSuccessor())
    }

    @Test
    fun getTrainingUnits() {
        val units: List<IUnit> = unitProvider.getUnits()
        assertEquals(3, units.size)
    }

    @Test
    fun initializeByTrainingUnit() {
        unitProvider.resetByUnit(testUnitProvider.secondTrainingUnit)
        assertEquals(testUnitProvider.thirdTrainingUnit, unitProvider.getSuccessor())
    }
}

class TestUnitProvider : UnitProvider(1, names) {
    lateinit var firstTrainingUnit: IUnit
    lateinit var secondTrainingUnit: IUnit
    lateinit var thirdTrainingUnit: IUnit

    override fun initialize() {
        firstTrainingUnit = mock(IUnit::class.java)
        secondTrainingUnit = mock(IUnit::class.java)
        thirdTrainingUnit = mock(IUnit::class.java)
        addUnitToStack(firstTrainingUnit)
        addUnitToStack(secondTrainingUnit)
        addUnitToStack(thirdTrainingUnit)
    }

    companion object {
        val firstExerciseDetails: IExerciseDetails = mock(IExerciseDetails::class.java)
        val secondExerciseDetails: IExerciseDetails = mock(IExerciseDetails::class.java)
        val thirdExerciseDetails: IExerciseDetails = mock(IExerciseDetails::class.java)
        var names: List<IExerciseDetails> = listOf(firstExerciseDetails, secondExerciseDetails, thirdExerciseDetails)
    }
}
