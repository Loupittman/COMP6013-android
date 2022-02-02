package com.google.mlkit.codelab.translate.main

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.google.mlkit.codelab.translate.util.ResultOrError
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
        private val base = "https://127.0.0.1:5000/container/"

        private const val TAG = "Containers"

        fun getContainer(container_id: String?, location: MediatorLiveData<ResultOrError>) {
            if (container_id.isNullOrEmpty()) {
                return

            }

            val stripped = container_id.filterNot { it.isWhitespace() }
            Log.d(TAG, "candidate container ID is: $stripped")

            val reg = Regex("[\\d]{7}")
            if (!reg.containsMatchIn(stripped)) {
                Log.d(TAG, "invalid container_id: $container_id")
                return

            }

            val match = reg.find(stripped)
            val candidate = match?.value
            Log.d(TAG, "valid candidate container_id: $candidate")

            val url = base + candidate

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
                    val json = JSONObject(data)
                    val container = Container(
                        container_id = json.getString("container_id"),
                        type = json.getString("type"),
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
                    Log.d(TAG, "JSON received: ${json.toString()}")
                    location.value = ResultOrError(container.description, null)
                },
                {
                    Log.d(TAG, "VolleyError: ${it.toString()}")
                }
            )
            Log.d(TAG, "queuing request for URL: $url")
            requestQueue.add(request)
        }
    }
}