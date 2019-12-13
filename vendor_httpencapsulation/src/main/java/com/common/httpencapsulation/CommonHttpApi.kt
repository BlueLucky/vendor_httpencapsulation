package com.common.httpencapsulation

import com.common.httpencapsulation.api.Service
import com.common.httpencapsulation.bean.ServerStatusBean
import com.common.httpencapsulation.ext.ReLoginException
import com.common.httpencapsulation.ext.ResultErrorException
import com.google.gson.JsonParseException
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * 获取网路访问的接口
 */
fun httpApi(): Service {
    return RetrofitHelper.retrofit.create(Service::class.java)
}

/**
 * 将字符传转换成class
 */
fun <T> Observable<String>.parseHook(clazz: Class<T>): Observable<T> {
    return this.compose<T>(ObservableTransformer { result ->
        return@ObservableTransformer result.map {
            StringConverterFactory.gson.fromJson(it, clazz)
        }
    }).globalCompose()
}

/**
 * 一些全局操作
 */
private fun <T> Observable<T>.globalCompose(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .handResult()
        .doOnError {
            check(!(BuildConfig.DEBUG && it !is ResultErrorException)) { "未处理异常" }
        }
}

/**
 * 统一处理异常情况
 */
private fun <T> Observable<T>.handResult(): Observable<T> {
    return this.doOnNext {
        if (it is ServerStatusBean) {
            if (!it.isReqSuccess()) {
                throw ResultErrorException(it.code, it.msg())
            } else {
                RetrofitHelper.tokenTimeOutCode?.let { timeOut ->
                    if (timeOut == it.code) {
                        //token过期处理
                        RetrofitHelper.tokenTimeOutFun?.invoke()
                        throw  ReLoginException()
                    }
                }
            }
        }
    }.onErrorResumeNext(Function { throwable ->
        val code: Int
        val msg: String
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace()
        }
        when (throwable) {
            is ReLoginException -> {
                code = RetrofitHelper.tokenTimeOutCode ?: -20001
                msg = "登录过期"
            }
            is HttpException -> {
                code = HTTP_ERROR
                msg = "其他网络错误"
            }
            is SocketTimeoutException -> {
                code = SERVICE_TIMEOUT
                msg = "连接超时"
            }
            is JSONException, is JsonParseException -> {
                code = PARSE_ERROR
                msg = "解析错误"
            }
            is ConnectException -> {
                code = NETWORD_ERROR
                msg = "网络错误，请检查您的网络并稍后再试"
            }
            is SSLHandshakeException -> {
                code = SSL_ERROR
                msg = "证书验证失败"
            }
            is UnknownHostException -> {
                code = HTTP_ERROR
                msg = "操作失败， 请检查网络连接"
            }
            is ResultErrorException -> {
                return@Function Observable.error(throwable)
            }
            else -> {
                code = UNKNOWN
                msg = "未知错误 - ${throwable.message}"
            }
        }
        return@Function Observable.error(ResultErrorException(code, msg))
    })
}

/**
 * 未知错误
 */
const val UNKNOWN = 1000
/**
 * 解析错误
 */
const val PARSE_ERROR = 1001
/**
 * 网络错误
 */
const val NETWORD_ERROR = 1002
/**
 * 协议出错
 */
const val HTTP_ERROR = 1003

/**
 * 证书出错
 */
const val SSL_ERROR = 1005
/**
 * 服务器响应超时
 */
const val SERVICE_TIMEOUT = 1006

/**
 * 网络错误
 */
const val NO_NETWORK = 10001