package coffeecode.co.learnasynctaskloader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coffeecode.co.learnasynctaskloader.R
import coffeecode.co.learnasynctaskloader.model.WeatherItems
import kotlinx.android.synthetic.main.weather_items.view.*


class WeatherAdapter: RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private val mData = ArrayList<WeatherItems>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_items, parent, false))

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(mData[position])
    }

    fun setData(items: ArrayList<WeatherItems>?){
        mData.clear()
        if (items != null) {
            mData.addAll(items)
        }
        notifyDataSetChanged()
    }

    fun addItem(items: WeatherItems){
        mData.add(items)
        notifyDataSetChanged()
    }

    fun clearData(){
        mData.clear()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bindItem(item: WeatherItems){
            itemView.textKota.text = item.name
            itemView.textTemp.text = item.temperature
            itemView.textDesc.text = item.description
        }
    }
}