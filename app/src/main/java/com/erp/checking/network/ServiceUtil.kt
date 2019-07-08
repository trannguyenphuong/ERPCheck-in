package com.erp.checking.network


import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.json.JSONException
import java.util.*

object ServiceUtil {
    const val COMPANY = "DXMT"
//    const val COMPANY = "DXBMT"
//    const val COMPANY = "DXDN"
//    const val COMPANY = "DXNMT"
//    const val COMPANY = "EME"

    fun getJSONCheckIn(
        action: String,
        method: String,
        type: String,
        tid: Int,
        formid: String,
        prospect: String,
        checkindate: String
    ): JsonElement {
        val query = JsonObject()
        try {
            query.addProperty("action", action)
            query.addProperty("method", method)
            query.addProperty("type", type)
            query.addProperty("tid", tid)
            val data = JsonObject()
            data.addProperty("formid", formid)
            data.addProperty("company", COMPANY)
            data.addProperty("prospect", prospect)
            data.addProperty("checkindate", checkindate)
            val arrayData = ArrayList<Any>()
            arrayData.add(data)
            val m = JsonArray()
            m.add(data)
            query.add("data", m)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return query
    }

    fun getJSONSearch(
        action: String,
        method: String,
        type: String,
        tid: Int
    ): JsonElement {
        val query = JsonObject()
        try {
            query.addProperty("action", action)
            query.addProperty("method", method)
            query.addProperty("type", type)
            query.addProperty("tid", tid)
            val data = JsonObject()
            data.addProperty("reportcode", "apiEmp")
            data.addProperty("limit", 1000)
            data.addProperty("start", 0)

            val queryFilters1 = JsonObject()

            queryFilters1.addProperty("name", "aempname")
            queryFilters1.addProperty("value", "")

            val mFilter = JsonArray()
            mFilter.add(queryFilters1)
            data.add("queryFilters", mFilter)

            val m = JsonArray()
            m.add(data)
            query.add("data", m)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return query
    }

    fun getJSONSearchCustomer(
        action: String,
        method: String,
        type: String,
        tid: Int,
        value: String
    ): JsonElement {
        val query = JsonObject()
        try {
            query.addProperty("action", action)
            query.addProperty("method", method)
            query.addProperty("type", type)
            query.addProperty("tid", tid)
            val data = JsonObject()
            data.addProperty("reportcode", "apiPRS")
            data.addProperty("limit", 25)
            data.addProperty("start", 0)

            val queryFilters1 = JsonObject()

            queryFilters1.addProperty("name", "atelephone")
            queryFilters1.addProperty("value", value)

            val mFilter = JsonArray()
            mFilter.add(queryFilters1)
            data.add("queryFilters", mFilter)

            val m = JsonArray()
            m.add(data)
            query.add("data", m)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return query
    }

    fun getJSONSignIn(
        address: String,
        employee: String,
        telephone: String,
        nadname: String,
        icard: String
    ): JsonElement {
        val query = JsonObject()
        try {
            query.addProperty("action", "FrmFdUserForm")
            query.addProperty("method", "import")
            query.addProperty("type", "rpc")
            query.addProperty("tid", 1)
            val data = JsonObject()
            data.addProperty("formid", "appprs")
            data.addProperty("company", COMPANY)
            data.addProperty("employee", employee)
            data.addProperty("icard", icard)
            data.addProperty("address", address)
            data.addProperty("telephone", telephone)
            data.addProperty("nadname", nadname)
            data.addProperty("nadcode", telephone)
            val arrayData = ArrayList<Any>()
            arrayData.add(data)
            val m = JsonArray()
            m.add(data)
            query.add("data", m)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return query
    }
}
