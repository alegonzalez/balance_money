package com.ale.balance_money.logic

import android.os.AsyncTask
import android.text.TextUtils.lastIndexOf
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * https://currency.getgeoapi.com/
 */
class ExchangeRate : AsyncTask<String, String, Double>() {

    /**
     * this function is used for get exchange rate with API
     * @param params
     * @return Double
     */
    override fun doInBackground(vararg params: String?): Double? {
        val chain = StringBuffer("")
        val from: String = getAcronymMoney(params[0].toString())
        val to: String = getAcronymMoney(params[1].toString())
        val amount = params[2]
        val url =
            URL("https://api.getgeoapi.com/api/v2/currency/convert?api_key=55ddd3f9e09c72f6701c52f6e5a22d3c2571b6d1&from=$from&to=$to&amount=$amount&format=json")
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val input: InputStream = BufferedInputStream(urlConnection.inputStream)
            val rd = BufferedReader(InputStreamReader(input));
            for (line in rd.readLine()) {
                chain.append(line);
            }
        } catch (e: Exception) {
            urlConnection.disconnect()
        } finally {
            urlConnection.disconnect()
        }
        var arrayData = chain.toString().split(",")
        arrayData = arrayData[5].split(":")
        arrayData = arrayData[1].split("}}")
        arrayData = listOf(arrayData[0].replace("\"", ""))
        return arrayData[0].toDouble()
    }

    /**
     * This function get acronym of money
     * @param money
     * @return String
     */
    private fun getAcronymMoney(money: String): String {
        return when (money) {
            "COLON" -> {
                "CRC"
            }
            "DOLLAR" -> {
                "USD"
            }
            else -> {
                "EUR"
            }
        }
    }
}