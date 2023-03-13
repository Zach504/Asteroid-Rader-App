package com.udacity.asteroid_radar_app

import android.content.res.Resources
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.udacity.asteroid_radar_app.data.Asteroid
import com.udacity.asteroid_radar_app.list.AsteroidListAdapter

@BindingAdapter("listData")
fun RecyclerView.listData(data: List<Asteroid>?){
    val adapter = this.adapter as AsteroidListAdapter
    adapter.submitList(data)
}


@BindingAdapter("asteroidHazardousStatusIcon")
fun ImageView.asteroidHazardousStatusIcon(hazardous: Boolean) {
    if(hazardous) {
        this.setImageResource(R.drawable.ic_status_potentially_hazardous)
        this.contentDescription = this.context.getString(R.string.hazardous_status)
    }
    else{
        this.setImageResource(R.drawable.ic_status_normal)
        this.contentDescription = this.context.getString(R.string.safe_status)
    }
}

@BindingAdapter("asteroidHazardousStatusImage")
fun ImageView.asteroidHazardousStatusImage(hazardous: Boolean) {
    if(hazardous) {
        this.setImageResource(R.drawable.asteroid_hazardous)
        this.contentDescription = this.context.getString(R.string.hazardous_status)
    }
    else{
        this.setImageResource(R.drawable.asteroid_safe)
        this.contentDescription = this.context.getString(R.string.safe_status)
    }
}

@BindingAdapter("double", "unit", requireAll = true)
fun TextView.loadDoubleWithUnits(double: Double, unit: String){
    this.text = "$double $unit"
}