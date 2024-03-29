package com.common.httpencapsulation

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 *
 */
object RetrofitHelper {
    /**
     * host
     */
    private var baseUrl: String? = null
    /**
     *
     */
    private var client: OkHttpClient? = null
    /**
     *
     */
    var tokenTimeOutCode:Int? = null
    /**
     * token过期操作方法
     */
    var tokenTimeOutFun:(()->Unit)?=null

    /**
     * Retrofit实例
     */
    val retrofit: Retrofit by lazy {
        val tmpClient = client ?: throw IllegalAccessError("not init RetrofitHelper")

        Retrofit.Builder()
            .baseUrl(baseUrl ?: "")
            .client(tmpClient)
            .addConverterFactory(StringConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    /**
     * 初始化
     */
    fun init(baseUrl: String, client: OkHttpClient,tokenTimeOutCode:Int?,tokenTimeOutFun:()->Unit) {
        this.baseUrl = baseUrl
        this.client = client
        this.tokenTimeOutCode = tokenTimeOutCode
        this.tokenTimeOutFun = tokenTimeOutFun
    }
}