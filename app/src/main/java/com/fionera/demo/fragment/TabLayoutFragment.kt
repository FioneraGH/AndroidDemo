package com.fionera.demo.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fionera.demo.R
import com.fionera.demo.util.LogCat
import kotlinx.android.synthetic.main.fragment_smart_tab_layout.view.*
import org.jetbrains.anko.async
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.net.URL

/**
 * Created by fionera on 15-10-3.
 */
class TabLayoutFragment : Fragment() {

    private val items = listOf(
            "Mon	6/23	-	Sunny	-	31/17",
            "Tue	6/24	-	Foggy	-	21/8",
            "Wed	6/25	-	Cloudy	-	22/17",
            "Thurs	6/26	-	Rainy	-	18/11",
            "Fri	6/27	-	Foggy	-	21/10",
            "Sat	6/28	-	TRAPPED	IN	WEATHER -	23/18",
            "Sun	6/29	-	Sunny	-	20/7"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_smart_tab_layout, container, false)
        async() {
            Request("").run()
            uiThread {
                activity.toast("done")
            }
        }
        view.rv_forecast_list.layoutManager = LinearLayoutManager(context)
        view.rv_forecast_list.adapter = ForecastListAdapter(items)
        return view
    }

    class ForecastListAdapter(var items: List<String>) : RecyclerView.Adapter<ForecastListAdapter.ViewHolder>() {
        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = items[position]
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
            return ViewHolder(TextView(parent.context))
        }

        class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
    }

    class Request(val url: String) {
        fun run() {
            val forecastJsonStr = URL(url).readText()
            LogCat.d(forecastJsonStr)
        }
    }
}
