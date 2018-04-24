package com.pts6.politie.service

import io.github.cdimascio.dotenv.Dotenv
import javax.ejb.Stateless

@Stateless
class AntaminenService {

    fun getAntaminenUrl() = Dotenv.load()["HTTP_ANTAMINEN_URL"]

    fun getVehicleById() {

    }

}