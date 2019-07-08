package com.erp.checking

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.erp.checking.network.AppApi
import com.erp.checking.network.HttpURLConnectionBuilder
import com.erp.checking.network.ServiceUtil
import com.erp.checking.network.response.SearchStaffResponse
import com.erp.checking.util.closeMessage
import com.erp.checking.util.showMessage
import com.erp.checking.util.showProgress
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.sign_in_activity.*


class SignInActivity : AppCompatActivity() {
    private var disposable: Disposable? = null
    private var mEmployID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_activity)

        val intent = intent
        if (intent != null) {
            val phoneIntent = intent.getStringExtra("sdt")
            edtPhoneNumber.setText(phoneIntent.toString())
        }
        btnSignIn.setOnClickListener {
            callApiSignIn()
        }
        callAPISearchStaff()
    }

    private fun initAdapter(listStaff: ArrayList<SearchStaffResponse.Staff>) {
        val adapter = StaffAdapter(this)
        adapter.addAll(listStaff)
        edtStaff!!.setAdapter(adapter)
        edtStaff!!.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) edtStaff!!.showDropDown() }
        edtStaff.setOnItemClickListener { mAdapter, _, position, _ ->
            mEmployID = (mAdapter.adapter.getItem(position) as SearchStaffResponse.Staff).employee!!
        }
    }

    private fun callApiSignIn() {
        val name = edtName.text.trim().toString()
        val address = edtAddress.text.trim().toString()
        val phone = edtPhoneNumber.text.trim().toString()
        val id = edtID.text.trim().toString()

        if (name.isNullOrEmpty()) {
            showMessage("Please input customer name")
            edtName.requestFocus()
        } else if (phone.isNullOrEmpty()) {
            showMessage("Please input customer phone")
            edtPhoneNumber.requestFocus()
        } else if (mEmployID.isNullOrEmpty()) {
            showMessage("Please select a staff")
            edtStaff.requestFocus()
        } else {

            val query = ServiceUtil.getJSONSignIn(address, mEmployID, phone, name, id)
            showProgress()
            val api = HttpURLConnectionBuilder.createApi(
                HttpURLConnectionBuilder.BASE_URL,
                AppApi::class.java
            )

            disposable = api.signIn("directrouter/index", query)
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    closeMessage()
                    if (response.result != null && response.result!!.success!!) {
                        runOnUiThread {
                            showMessage(getString(R.string.sign_in_ok, name)) {
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
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
    }

    private fun callAPISearchStaff() {
        showProgress()
        val api = HttpURLConnectionBuilder.createApi(
            HttpURLConnectionBuilder.BASE_URL,
            AppApi::class.java
        )

        val query = ServiceUtil.getJSONSearch(
            "FrmFdReport",
            "qry",
            "rpc",
            1
        )
        disposable = api.search("directrouter/index", query)
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                closeMessage()
                if (response.result != null && response.result!!.success!!) {
                    val data = response.result!!.data
                    if (data != null) {
                        if (data.isNotEmpty()) {
                            runOnUiThread {
                                initAdapter(data)
                            }
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
}
