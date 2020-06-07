package com.acoria.unittimer.unittimer_api.units

interface IUnit{

    fun getId(): String
    fun getTitle(): String?
    fun getLength(): Int
    fun getInfoImage(): Int?
    fun isOneSided(): Boolean
    fun setTitle(title: String)
    fun setInfoImage(infoImage: Int?)

}