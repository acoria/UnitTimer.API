package com.acoria.unittimer.unittimer_api

import org.mockito.Mockito

//helper function to cast the null to actual object type
//overrides ArgumentMatchers.any
//https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
fun <T> any(): T {
    Mockito.any<T>()
    return uninitialized()
}
@Suppress("UNCHECKED_CAST")
private fun <T> uninitialized(): T = null as T