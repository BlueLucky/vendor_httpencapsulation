package com.common.httpencapsulation

import com.common.httpencapsulation.api.Service
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers

/**
 * 获取网路访问的接口
 */
fun httpApi(): Service {
    return RetrofitHelper.retrofit.create(Service::class.java)
}

/**
 * 将字符传转换成class
 */
fun <T> Observable<String>.parseHook(clazz: Class<T>):Observable<T>{
    return this.compose<T>(ObservableTransformer { result ->
        return@ObservableTransformer result.map {
            StringConverterFactory.gson.fromJson(it,clazz)
        }
    })
}

/**
 * 一些全局操作
 */
fun <T> Observable<T>.globalCompose(clazz: Class<T>):Observable<T>{
    return this.subscribeOn(Schedulers.io())
}