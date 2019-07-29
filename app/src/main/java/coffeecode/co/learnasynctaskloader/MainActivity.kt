package coffeecode.co.learnasynctaskloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import coffeecode.co.learnasynctaskloader.model.WeatherItems
import coffeecode.co.learnasynctaskloader.adapter.WeatherAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<ArrayList<WeatherItems>>{

    companion object{
        private const val EXTRAS_CITY = "EXTRAS_CITY"
    }

    private lateinit var adapter: WeatherAdapter

    private  val bundle = Bundle()

    private var city: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setAdapter()

        city = edtCity.text.toString()
        bundle.putString(EXTRAS_CITY, city)

        supportLoaderManager.initLoader(0, bundle, this)

        btnCity.setOnClickListener {
            city = edtCity.text.toString()
            if (TextUtils.isEmpty(city)) return@setOnClickListener
            bundle.putString(EXTRAS_CITY, city)
            supportLoaderManager.restartLoader(0, bundle, this)
        }
    }

    private fun setAdapter() {
        adapter = WeatherAdapter()
        adapter.notifyDataSetChanged()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<WeatherItems>> {
        var cities: String? = ""
        if (args != null) {
            cities = args.getString(EXTRAS_CITY)
        }
        return MyAsyncTaskLoader(this, cities)
    }

    override fun onLoadFinished(loader: Loader<ArrayList<WeatherItems>>, data: ArrayList<WeatherItems>?) {
        data?.let { adapter.setData(it) }
    }

    override fun onLoaderReset(loader: Loader<ArrayList<WeatherItems>>) {
        adapter.setData(null)
    }
}
