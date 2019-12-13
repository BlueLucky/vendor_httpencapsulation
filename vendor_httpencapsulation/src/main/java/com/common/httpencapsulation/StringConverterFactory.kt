package com.common.httpencapsulation

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets.UTF_8

/**
 *
 */
class StringConverterFactory private constructor() : Converter.Factory() {

    companion object {
        val gson: Gson by lazy { Gson() }
        fun create(): StringConverterFactory {
            return StringConverterFactory()
        }
    }

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>,
                                      methodAnnotations: Array<Annotation>, retrofit: Retrofit)
            : Converter<*, RequestBody>? {
        return GsonRequestBodyConverter(gson, gson.getAdapter(TypeToken.get(type)))
    }

    override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return Converter<ResponseBody, String> { value ->
            value.use {
                return@Converter it.string()
            }
        }
    }

    private class GsonRequestBodyConverter<T>(val gson: Gson, val adapter: TypeAdapter<T>) : Converter<T, RequestBody> {

        override fun convert(value: T): RequestBody? {
            val buffer = Buffer()
            val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
            val jsonWriter = gson.newJsonWriter(writer)
            adapter.write(jsonWriter, value)
            jsonWriter.close()
            return RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), buffer.readByteString())
        }
    }
}