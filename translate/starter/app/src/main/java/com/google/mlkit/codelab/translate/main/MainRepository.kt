package com.google.mlkit.codelab.translate.main

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import org.json.JSONArray
import org.json.JSONObject

data class Container(
    val container_id : String,
    val type : String,
    val description : String,
    val vle : String,
    val model_mix : String,
    val empties_storage : String,
    val empties_lane : String,
    val jis_route : String,
    val length : String,
    val width : String,
    val height : String,
    val weight : String,
    val haulier_id : String,
    val load_unit_id : String
)

class MainRepository {
    companion object {
        private val base = "http://127.0.0.1:5000/"

        fun getContainer(container_id: String?): Container? {
            if (container_id.isNullOrEmpty()) return null

            val url = base + container_id

            var container: Container? = null

            // Set up the network to use HttpURLConnection as the HTTP client.
            val network = BasicNetwork(HurlStack())

            // Instantiate the RequestQueue with the cache and network. Start the queue.
            val requestQueue = RequestQueue(NoCache(), network).apply {
                start()
            }

            val request = StringRequest(Request.Method.GET,
                url,
                { response ->
                    val data = response.toString()
                    var json = JSONObject(data)
                    container = Container(
                        container_id = json.getString("container_id"),
                        type = json.getString(""),
                        description = json.getString("description"),
                        vle = json.getString("vle"),
                        model_mix = json.getString("model_mix"),
                        empties_storage = json.getString("empties_storage"),
                        empties_lane = json.getString("empties_lane"),
                        jis_route = json.getString("jis_route"),
                        length = json.getString("length"),
                        width = json.getString("width"),
                        height = json.getString("height"),
                        weight = json.getString("weight"),
                        haulier_id = json.getString("haulier_id"),
                        load_unit_id = json.getString("load_unit_id")
                    )
                },
                {
                    Log.d("Containers", "VolleyError: " + it.toString())
                }
            )
            requestQueue.add(request)

            return container
        }
    }
}