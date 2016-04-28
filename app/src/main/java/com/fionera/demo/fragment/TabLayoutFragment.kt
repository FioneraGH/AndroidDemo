package com.fionera.demo.fragment

import android.animation.Animator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fionera.demo.R
import com.fionera.demo.util.LogCat
import com.fionera.demo.util.ShowToast
import com.fionera.demo.util.SimpleAnimatorListener
import com.google.gson.Gson
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import java.net.URL

/**
 * Created by fionera on 15-10-3.
 */
class TabLayoutFragment : Fragment() {

    var rv_forecast_list: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = with(context) {
            verticalLayout {
                padding = dip(16)
                rv_forecast_list = recyclerView {
                    clipToPadding = false
                }.lparams {
                    width = matchParent
                    height = matchParent
                }
            }
        }
        async() {
            val result = Request("246000").execute()
            uiThread {
                rv_forecast_list?.adapter = ForecastListAdapter(result)
            }
        }
        rv_forecast_list?.layoutManager = LinearLayoutManager(context)

        val s = TypedClass(typedFun(TypedClass(context).value))

        val n: String? = null

        val sr: TypedClass<Any> = s.let { TypedClass(0) }

        activity.toast("${sr.toString()} ${n.let { n }}")
        return view
    }

    class TypedClass<out X>(param: X) {
        val value: X = param
    }

    fun <X> typedFun(item: X): X = item

    class ForecastListAdapter(var item: ForecastResult) : RecyclerView.Adapter<ForecastListAdapter.ViewHolder>() {
        override fun getItemCount(): Int = item.list.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.find<TextView>(1).text = "${item.list[position].speed}"
            holder.itemView.setOnClickListener { it.click({ ShowToast.show("click $position") }) }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
            return ViewHolder(with(parent.context) {
                verticalLayout {
                    backgroundColor = ContextCompat.getColor(parent.context, R.color.blue2)
                    textView {
                        id = 1
                        gravity = Gravity.CENTER_HORIZONTAL
                        backgroundColor = ContextCompat.getColor(parent.context, R.color.grey2)
                    }.lparams {
                        margin = dip(16)
                    }
                }
            })
        }

        fun View.click(run: () -> Unit) {
            animate().scaleX(0.9f).scaleY(0.9f).setListener(object : SimpleAnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    animate().scaleX(1.0f).scaleY(1.0f).setListener(object : SimpleAnimatorListener() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            run()
                        }
                    }).start()
                }
            }).start()
        }

        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    }

    class Request(val zipcode: String) {

        companion object {
            private val URL = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&units=metric&cnt=7"
            private val APP_ID = "15646a06818f61f7b8d7823ca833e1ce"
            private val COMPLETE_URL = "$URL&APPID=$APP_ID&q="
        }

        fun execute(): ForecastResult {
            val forecastJsonStr = URL(COMPLETE_URL + zipcode).readText()
            LogCat.d(forecastJsonStr)
            return Gson().fromJson(forecastJsonStr, ForecastResult::class.java)
        }
    }

    data class ForecastResult(val city: City, val list: List<Forecast>)

    data class City(val id: Long, val name: String, val coord: Coordinates,
                    val country: String, val population: Int)

    data class Coordinates(val lon: Float, val lat: Float)

    data class Forecast(val dt: Long, val temp: Temperature, val pressure: Float,
                        val humidity: Int, val weather: List<Weather>,
                        val speed: Float, val deg: Int, val clouds: Int,
                        val rain: Float)

    data class Temperature(val day: Float, val min: Float, val max: Float,
                           val night: Float, val eve: Float, val morn: Float)

    data class Weather(val id: Long, val main: String, val description: String, val icon: String)
}
