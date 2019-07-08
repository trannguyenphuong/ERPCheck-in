package com.erp.checking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.erp.checking.network.AppApi
import com.erp.checking.network.HttpURLConnectionBuilder
import com.erp.checking.network.ServiceUtil
import com.erp.checking.network.response.SearchCustomerResponse
import com.erp.checking.util.closeMessage
import com.erp.checking.util.showMessage
import com.erp.checking.util.showProgress
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.search_activity.*


class SearchActivity : AppCompatActivity() {
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    tvSearch.visibility = View.GONE
                } else {
                    tvSearch.visibility = View.VISIBLE
                }
            }
        })

        tvSearch.setOnClickListener { callAPISearch(edtSearch.text.toString().trim()) }
    }

    private fun callAPISearch(value: String) {
        showProgress()
        val api = HttpURLConnectionBuilder.createApi(
            HttpURLConnectionBuilder.BASE_URL,
            AppApi::class.java
        )

        val query = ServiceUtil.getJSONSearchCustomer(
            "FrmFdReport",
            "qry",
            "rpc",
            1,
            value
        )
        disposable = api.searchCustomer("directrouter/index", query)
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                closeMessage()
                if (response.result != null && response.result!!.success!!) {
                    val data = response.result!!.data
                    if (data != null) {
                        if (data.isNotEmpty()) {
                            gotoMain(data[0])
                        } else {
                            gotoSignIn()
                        }
                    } else {
                        runOnUiThread {
                            showMessage(getString(R.string.error))
                        }
                    }
                } else {
                    runOnUiThread {
                        showMessage(getString(R.string.error))
                    }
                }
            }, ({
                closeMessage()
                runOnUiThread {
                    showMessage(getString(R.string.error))
                }
            }))
    }

    //Call api, nếu sv trả về rỗng thì redirect tới from đăng ký
    private fun gotoSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        intent.putExtra("sdt", edtSearch.text.toString().trim())
        startActivity(intent)
    }

    private fun gotoMain(prospect: SearchCustomerResponse.DataR?) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra("prospect", prospect)
        })
        this.finish()
    }
}
