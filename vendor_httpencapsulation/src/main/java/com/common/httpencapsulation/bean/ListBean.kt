package com.common.httpencapsulation.bean

/**
 */
open class ListBean<T : IApiBean> : Bean<ListX<T>>() {
    override var data: ListX<T> = ListX()
}

class ListX<T : IApiBean> : IApiBean {
    /**
     * current (integer, optional):
     * 当前页 ,
     * pages (integer, optional):
     * 总页数 ,
     * size (integer, optional):
     * 每页显示条数 ,
     * total (integer, optional):
     * 总条数
     */
    var current: Int = 0
    var pages: Int = 0
    var size: Int = 0
    var total: Int = 0

    var records: List<T> = arrayListOf()

    /**
     * 扩展字段，适用于特定的场景
     */
    var expansion: String? = null


    fun hasMore(): Boolean {
        return false
    }

    fun isFirstPage(): Boolean {
        return current <= 1
    }
}