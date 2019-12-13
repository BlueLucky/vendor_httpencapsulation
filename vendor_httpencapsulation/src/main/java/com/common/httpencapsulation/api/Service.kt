package com.common.httpencapsulation.api

import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 网络访问接口
 *  @author lucky
 */
interface Service{
    @GET
    fun httpGet(@Url url: String, @QueryMap body: Map<String, String> = mapOf()): Observable<String>

    @FormUrlEncoded
    @POST
    fun postForm(@Url url: String, @Body body: Map<String, String> = mapOf()): Observable<String>

    @POST
    fun postJson(@Url url: String, @Body body: Map<String, String> = mapOf()
                 ,@Header("signatureKey")signatureKey:String?=""): Observable<String>

    @POST
    fun postBody(@Url url: String, @Body body: RequestBody
                 ,@Header("signatureKey")signatureKey:String?=""): Observable<String>
}