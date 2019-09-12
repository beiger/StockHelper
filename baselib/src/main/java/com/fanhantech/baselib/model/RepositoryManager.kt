package com.fanhantech.baselib.model

import com.fanhantech.baselib.app.BaseApplication
import com.fanhantech.baselib.model.retrofit.KotlinCallAdatpterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tamic.novate.cache.CookieCacheImpl
import com.tamic.novate.cookie.NovateCookieManager
import com.tamic.novate.cookie.SharedPrefsCookiePersistor

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.Protocol
import java.util.*
import java.util.concurrent.TimeUnit

//const val BASE_URL = "http://10.0.0.19"
const val BASE_URL = "https://www.fanhantech.com"

object RepositoryManager {
        private val mRetrofit: Retrofit
        val cookieManager = NovateCookieManager(CookieCacheImpl(), SharedPrefsCookiePersistor(BaseApplication.context))

        init {
                //声明日志类
                val interceptor = ChuckInterceptor(BaseApplication.context)
                //自定义OkHttpClient
                val okHttpClientBuilder = OkHttpClient.Builder()
                //添加拦截器
                okHttpClientBuilder.addInterceptor(interceptor)
                okHttpClientBuilder
                        .followRedirects(false)
                        .followSslRedirects(false)
                        .cookieJar(cookieManager)
                        .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)

                mRetrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL) //设置网络请求的Url地址
                        .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addCallAdapterFactory(KotlinCallAdatpterFactory.create())
                        .client(okHttpClientBuilder.build())
                        .build()
        }

        fun <T> obtainRetrofitService(service: Class<T>): T = mRetrofit.create(service)
}
