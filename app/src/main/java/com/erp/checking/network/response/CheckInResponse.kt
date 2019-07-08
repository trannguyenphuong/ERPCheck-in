package com.erp.checking.network.response

import java.io.Serializable

class CheckInResponse : Serializable {
    var result: Result? = null

    inner class Result : Serializable {
        var success: Boolean? = false
        var title: String? = ""
        var message: String? = ""
    }
}
