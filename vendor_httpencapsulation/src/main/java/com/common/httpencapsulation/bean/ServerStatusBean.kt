package com.common.httpencapsulation.bean

open class ServerStatusBean {
    var code: Int = -1
    var msg: String = ""

    fun msg(): String {
        return msg ?: "获取数据失败"
    }

    open fun isReqSuccess(): Boolean {
        return code == 0
    }
}