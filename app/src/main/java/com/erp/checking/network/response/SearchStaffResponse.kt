package com.erp.checking.network.response

import java.io.Serializable


class SearchStaffResponse : Serializable {
    var result: Result? = null

    inner class Result : Serializable {
        var success: Boolean? = false
        var title: String? = ""
        var message: String? = ""
        var data: ArrayList<Staff>? = null
    }

    inner class Staff : Serializable {
        var employee: String? = ""
        var name: String? = ""

        override fun toString(): String {
            return name!!
        }
    }
}
