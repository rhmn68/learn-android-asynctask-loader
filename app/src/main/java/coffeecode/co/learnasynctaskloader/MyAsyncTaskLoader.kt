package coffeecode.co.learnasynctaskloader

import android.content.Context
import androidx.loader.content.AsyncTaskLoader
import coffeecode.co.learnasynctaskloader.model.WeatherItems
import com.loopj.android.http.SyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception


class MyAsyncTaskLoader(context: Context, private var cities: String?) : AsyncTaskLoader<ArrayList<WeatherItems>>(context) {

    companion object{
        private const val API_KEY = "a6d3678de683ec9de8944b08ff50e349"
    }

    private var mData: ArrayList<WeatherItems>? = null
    private var mHasResult = false

    init {
        onContentChanged()
    }

    override fun onStartLoading() {
        if (takeContentChanged()){
            forceLoad()
        }else if (mHasResult){
            deliverResult(mData)
        }
    }

    override fun deliverResult(data: ArrayList<WeatherItems>?) {
        mData = data
        mHasResult = true
        super.deliverResult(data)
    }

    override fun onReset() {
        super.onReset()
        onStopLoading()
        if (mHasResult){
            mData = null
            mHasResult = false
        }
    }

    // Format search kota url JAKARTA = 1642911 ,BANDUNG = 1650357, SEMARANG = 1627896
    // http://api.openweathermap.org/data/2.5/group?id=1642911,1650357,1627896&units=metric&appid=API_KEY

    override fun loadInBackground(): ArrayList<WeatherItems>? {
        val client = SyncHttpClient()
        val weatherItemises = ArrayList<WeatherItems>()
        val url = "http://api.openweathermap.org/data/2.5/group?id=$cities&units=metric&appid=$API_KEY"

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onStart() {
                super.onStart()
                useSynchronousMode = true
            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    if (responseBody != null){
                        val result = String(responseBody)
                        val responseObject = JSONObject(result)
                        val list = responseObject.getJSONArray("list")

                        for (i in 0 until list.length()) {
                            val weather = list.getJSONObject(i)
                            val weatherItems = WeatherItems(weather)
                            weatherItemises.add(weatherItems)
                        }
                    }

                }catch (e: Exception){
                    //Jika terjadi error pada saat parsing maka akan masuk ke catch()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                //Jika response gagal maka , do nothing
            }
        })
        return weatherItemises
    }
}