package com.erp.checking.network

import okhttp3.Interceptor
import okhttp3.Response

class HttpAuthorization : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        //cong ty (DXMT)
        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("ClientId", "API@DXMT")
        builder.addHeader("TvcToken", "DXST934C-6DFA-478A-B003-908CBDE701F0")
        builder.addHeader("SecurityKey", "385D-A85B-6C1A-E9A7-D3D3-8EC0-366D-6740-819B-902C")

//        //cong ty (DXBMT)
//        builder.addHeader("Content-Type", "application/json")
//        builder.addHeader("ClientId", "API@DXBMT")
//        builder.addHeader("TvcToken", "2B1A15DF-9970-468D-B7DB-5B7900F1AE8C")
//        builder.addHeader("SecurityKey", "ABD0-6E86-A5CD-8A8B-D3FE-5F32-79D4-657A-2828-2DCD")
//
//        //cong ty (DXDN)
//        builder.addHeader("Content-Type", "application/json")
//        builder.addHeader("ClientId", "API@DXDN")
//        builder.addHeader("TvcToken", "43456354-8C4D-4AE8-A5F0-4E0FC0FCFB30")
//        builder.addHeader("SecurityKey", "2AD8-8739-3691-F48F-E6DE-2580-A885-DBD3-491E-5E35")
//
//        //cong ty (DXNMT)
//        builder.addHeader("Content-Type", "application/json")
//        builder.addHeader("ClientId", "API@DXNMT")
//        builder.addHeader("TvcToken", "ED932977-6544-4567-9FE5-2433BBF22362")
//        builder.addHeader("SecurityKey", "8BEB-9E7B-F7C4-CB57-B43F-349B-3A36-CB49-711B-E7B7")
//
//        //cong ty (EME)
//        builder.addHeader("Content-Type", "application/json")
//        builder.addHeader("ClientId", "API@EME")
//        builder.addHeader("TvcToken", "D6CC45BD-2E2D-4F80-B719-F44F143DFE6C")
//        builder.addHeader("SecurityKey", "A49D-81ED-3C87-6DC6-5349-3346-9724-2D40-1437-652C")

        return chain.proceed(builder.build())
    }
}