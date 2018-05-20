package com.pts6.politie.service

import com.google.gson.Gson
import com.pts6.politie.fromJson
import com.pts6.politie.service.api.CarResponse
import com.pts6.politie.service.api.JwtRequestParameters
import io.github.cdimascio.dotenv.Dotenv
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import javax.ejb.Stateless

@Stateless
class AntaminenService {

    private val mediaTypeJson = MediaType.parse("application/json; charset=utf-8")

    fun getAntaminenUrl() = Dotenv.load()["HTTP_ANTAMINEN_URL"]
    fun getAntaminenEmail() = Dotenv.load()["HTTP_ANTAMINEN_EMAIL"]
    fun getAntaminenPassword() = Dotenv.load()["HTTP_ANTAMINEN_PASSWORD"]

    private fun getHttpClient() =
            OkHttpClient
                    .Builder()
                    .build()!!

    private var jwt_token: String? = null

    fun retrieveJwtToken(forceRefresh: Boolean = false): String {
        if (this.jwt_token != null) {
            return this.jwt_token!!
        }

        val jwtRequestParameters = JwtRequestParameters(this.getAntaminenEmail()!!, this.getAntaminenPassword()!!)

        val postBody = RequestBody.create(this.mediaTypeJson, Gson().toJson(jwtRequestParameters))
        val antaminenLoginUrl = "${this.getAntaminenUrl()}/accounts/login"
        val request = Request
                .Builder()
                .url(antaminenLoginUrl)
                .post(postBody)
                .header("Accept", "application/json")
                .build()

        // Check je VPN verbinding als dit niet werkt
        val requestResult = this.getHttpClient().newCall(request).execute()!!
        val bodyString = requestResult.body()!!.string()!!
        val responseMap = Gson().fromJson<Map<String, Any>>(bodyString)

        val token = responseMap["token"] as String
        val owner = responseMap["owner"]
        this.jwt_token = token
        return token
    }

    fun getVehicles(): List<CarResponse> {
        val antaminenCarUrl = "${this.getAntaminenUrl()}/accounts/cars"
        val request = Request
                .Builder()
                .url(antaminenCarUrl)
                .header("Authorization", this.retrieveJwtToken())
                .build()

        val requestResult = this.getHttpClient().newCall(request).execute()!!
        val bodyString = requestResult.body()!!.string()!!
        return Gson().fromJson(bodyString)
    }

    fun getVehicleByLicensePlate(licensePlate:String) {
        val antaminenCarUrl = "${this.getAntaminenUrl()}/vehicles/licensePlate/$licensePlate/history/ownership"
        val request = Request
                .Builder()
                .url(antaminenCarUrl)
//                .header("Authorization", this.retrieveJwtToken())
                .build()

        val requestResult = this.getHttpClient().newCall(request).execute()!!
        val bodyString = requestResult.body()!!.string()!!
        return Gson().fromJson(bodyString)
    }

}
