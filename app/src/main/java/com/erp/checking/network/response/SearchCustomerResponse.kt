package com.erp.checking.network.response

import java.io.Serializable

class SearchCustomerResponse : Serializable {
    var result: Result? = null

    inner class Result : Serializable {
        var success: Boolean? = false
        var title: String? = ""
        var message: String? = ""
        var data: Array<DataR>? = null
    }

    inner class DataR : Serializable {
        var name: String? = ""
        var prospect: String? = ""
    }
}
