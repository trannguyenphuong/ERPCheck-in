package com.erp.checking.network

import com.erp.checking.network.response.CheckInResponse
import com.erp.checking.network.response.SearchCustomerResponse
import com.erp.checking.network.response.SearchStaffResponse
import com.erp.checking.network.response.SignInResponse
import com.google.gson.JsonElement
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url


interface AppApi {
    @POST
    fun checkin(@Url url: String, @Body query: JsonElement): Observable<CheckInResponse>

    @POST
    fun search(@Url url: String, @Body query: JsonElement): Observable<SearchStaffResponse>

    @POST
    fun searchCustomer(@Url url: String, @Body query: JsonElement): Observable<SearchCustomerResponse>

    @POST
    fun signIn(@Url url: String, @Body query: JsonElement): Observable<SignInResponse>
}