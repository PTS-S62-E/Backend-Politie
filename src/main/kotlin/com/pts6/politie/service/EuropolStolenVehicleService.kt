package com.pts6.politie.service

import com.google.gson.Gson
import com.pts6.politie.domain.EuroStolenVehicle
import com.pts6.politie.fromJson
import com.pts6.politie.toJson
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import javax.ejb.Stateless

@Stateless
class EuropolStolenVehicleService : IStolenVehicleService {

    private val baseUrl = "http://localhost:3000/stolenVehicles/"
    private val mediaTypeJson = MediaType.parse("application/json")
    private val httpClient = OkHttpClient.Builder().build()

    override fun insertStolenVehicle(euroStolenVehicle: EuroStolenVehicle): Boolean {
        val requestBody = RequestBody.create(mediaTypeJson, euroStolenVehicle.toJson())
        val request = Request.Builder()
                .url(baseUrl)
                .post(requestBody)
                .build()
        val response = this.httpClient.newCall(request).execute()
        return response.isSuccessful
    }

    override fun getVehicleByLicensePlate(licensePlate: String): EuroStolenVehicle {
        val request = Request.Builder().url("$baseUrl$licensePlate").get().build()
        val response = this.httpClient.newCall(request).execute()
        return Gson().fromJson<EuroStolenVehicle>(response.body()!!.string())
    }

    override fun updateStolenVehicle(euroStolenVehicle: EuroStolenVehicle): Boolean {
        val requestBody = RequestBody.create(mediaTypeJson, euroStolenVehicle.toJson())
        val request = Request.Builder()
                .url("$baseUrl${euroStolenVehicle.id}")
                .put(requestBody)
                .build()
        val response = this.httpClient.newCall(request).execute()
        return response.isSuccessful
    }

    override fun deleteStolenVehicle(id: String): Boolean {
        val request = Request.Builder()
                .url("$baseUrl$id")
                .delete()
                .build()
        val response = this.httpClient.newCall(request).execute()
        return response.isSuccessful
    }

    override fun getStolenVehicles(): List<EuroStolenVehicle> {
        val request = Request.Builder()
                .url("$baseUrl?country=FI")
                .get()
                .build()
        val response = this.httpClient.newCall(request).execute()
        return Gson().fromJson(response.body()!!.string()) as List<EuroStolenVehicle>
    }
}