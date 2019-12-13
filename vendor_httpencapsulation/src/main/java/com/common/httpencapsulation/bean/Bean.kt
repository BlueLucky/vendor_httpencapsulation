package com.common.httpencapsulation.bean


abstract class Bean<T:IApiBean>:ServerStatusBean(){
    abstract var data:T
}

abstract class Bean2<T:IApiBean>:ServerStatusBean(){
    abstract var data:List<T>
}

class IntBean : ServerStatusBean() {
    var data: Int = 0
}

class FloatBean : ServerStatusBean() {
    var data: Float = 0f
}

class DoubleBean : ServerStatusBean() {
    var data: Double = 0.0
}

class StringBean : ServerStatusBean() {
    var data: String = ""
}

class BooleanBean : ServerStatusBean() {
    var data: Boolean = false
}