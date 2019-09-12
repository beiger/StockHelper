package com.fanhantech.baselib.model.retrofit

import retrofit2.*
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class KotlinCall<R>(val originCall: Call<R>) {

        inline fun subscribe(
                 onSuccess: (R) -> Unit,
                 onError: (Throwable) -> Unit
        ) {
                try {
                        val response: Response<R> = originCall.execute()
                        if (response.isSuccessful) {
                                val body = response.body()
                                if (body == null) {
                                        onError(Throwable("response body null"))
                                } else {
                                        onSuccess(body)
                                }
                        } else {
                                onError(HttpException(response))
                        }
                } catch (e: IOException) {
                        onError(e)
                }
        }

        fun subscribeWithReturn(
                onSuccess: ((R) -> R)? = null,
                onError: ((Throwable) -> Unit)? = null
        ): R? {
                try {
                        val response: Response<R> = originCall.execute()
                        return if (response.isSuccessful) {
                                val body = response.body()
                                if (body == null) {
                                        if (onError != null) {
                                                onError(Throwable("response body null"))
                                        }
                                        null
                                } else {
                                        if (onSuccess == null) {
                                                body
                                        } else {
                                                onSuccess(body)
                                        }
                                }
                        } else {
                                if (onError != null) {
                                        onError(HttpException(response))
                                }
                                null
                        }
                } catch (e: IOException) {
                        if (onError != null) {
                                onError(e)
                        }
                        return null
                }
        }
}

class KotlinCallAdapter<R>(private val responseType: Type): CallAdapter<R, KotlinCall<R>> {

        override fun adapt(call: Call<R>): KotlinCall<R> {
                return KotlinCall(call)
        }

        override fun responseType(): Type {
                return responseType
        }
}

class KotlinCallAdatpterFactory private constructor(): CallAdapter.Factory() {

        companion object {
                fun create(): KotlinCallAdatpterFactory {
                        return KotlinCallAdatpterFactory()
                }
        }

        override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
                val rawType = getRawType(returnType)
                return if (rawType == KotlinCall::class.java) {
                        if (returnType !is ParameterizedType) {
                                throw IllegalStateException(
                                        "KotlinCall return type must be parameterized as KotlinCall<Foo> or KotlinCall<out Foo>")
                        }
                        val callReturnType = getParameterUpperBound(0, returnType)
                        KotlinCallAdapter<Any>(callReturnType)
                } else {
                        null
                }
        }

}