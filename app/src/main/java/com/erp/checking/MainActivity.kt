package com.erp.checking

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.erp.checking.network.AppApi
import com.erp.checking.network.HttpURLConnectionBuilder
import com.erp.checking.network.HttpURLConnectionBuilder.BASE_URL
import com.erp.checking.network.ServiceUtil
import com.erp.checking.network.response.SearchCustomerResponse
import com.erp.checking.util.DateUtil
import com.erp.checking.util.closeMessage
import com.erp.checking.util.showMessage
import com.erp.checking.util.showProgress
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var disposable: Disposable? = null
    private var customerName: String? = null

    companion object {
        private const val REQUEST_QR = 1
        private const val REQUEST_SEARCH = 3
        private const val REQUEST_CAMERA = 2
        const val QR_CODE_DATA = "QR_CODE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle(ServiceUtil.COMPANY + "- Checkin")
        tvScan.setOnClickListener {
            val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            )

            if (permission == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(Intent(this, ScanQRCodeActivity::class.java), REQUEST_QR)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA
                )
            }
        }
        tvSearch.setOnClickListener {
            startActivityForResult(Intent(this, SearchActivity::class.java), REQUEST_SEARCH)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CAMERA -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                } else {
                    startActivityForResult(Intent(this, ScanQRCodeActivity::class.java), REQUEST_QR)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_QR && resultCode == Activity.RESULT_OK && data != null) {
            val qrCode = data.getStringExtra(QR_CODE_DATA) ?: ""
            val qrCodes = qrCode.split(",")
            if (qrCodes.isNotEmpty()) {
                checkin(qrCodes[0])
            }
            if (qrCodes.size > 1) {
                customerName = qrCodes[1]
            }
        }
        if (requestCode == REQUEST_SEARCH && resultCode == Activity.RESULT_OK && data != null) {
            val model = data.getSerializableExtra("prospect") as SearchCustomerResponse.DataR
            checkin(model.prospect!!)
            customerName = model.name
        }
    }

    private fun checkin(code: String) {
        val api = HttpURLConnectionBuilder.createApi(
            BASE_URL,
            AppApi::class.java
        )

        val query = ServiceUtil.getJSONCheckIn(
            "FrmFdUserForm",
            "import",
            "rpc",
            1,
            "pcheckin",
            code,
            DateUtil.formatDateInbox(Date())
        )
        showProgress()
        disposable = api.checkin("directrouter/index", query)
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                closeMessage()
                if (response.result != null && response.result!!.success!!) {
                    runOnUiThread {
                        showMessage(getString(R.string.check_in_ok, customerName))
                    }
                }
            }, ({
                closeMessage()
                runOnUiThread {
                    showMessage(getString(R.string.check_in_failed_2))
                }
            }))
    }

}
