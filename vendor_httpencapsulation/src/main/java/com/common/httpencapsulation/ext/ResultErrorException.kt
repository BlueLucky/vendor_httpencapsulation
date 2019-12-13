package com.common.httpencapsulation.ext

import com.common.httpencapsulation.BuildConfig

/**
 *
 */
data class ResultErrorException(val code: Int, val msg: String) : Exception() {
    override fun toString(): String {
        return if (BuildConfig.DEBUG) {
            "$msg($code)"
        } else msg
    }
}