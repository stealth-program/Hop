package com.hop

import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.adapter_beer.view.*

class BeerAdapter(private val items : ArrayList<Beer>, private val context: Context): RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of beers in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_beer, parent, false))
    }

    // Binds each beer in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.tvBeerName?.text = items[position].beerName
        holder?.tvBrewery?.text = items[position].brewery
        holder?.tvCreationDate?.text = items[position].creationDate
        holder?.tvBeerStyle?.text = items[position].beerStyle
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each beer to
    val tvBeerName = view.tvBeerName!!
    val tvBrewery = view.tvBrewery!!
    val tvCreationDate = view.tvCreationDate!!
    val tvBeerStyle = view.tvBeerStyle!!

    val context = view.getContext()

    init {
        view.setOnClickListener { v: View ->
            val intent = Intent(context, AddBeerActivity::class.java)
            context.startActivity(intent)
        }
    }

    init {
        view.setOnLongClickListener { v: View ->
            var position: Int = getAdapterPosition()

            Snackbar.make(v, "Click detected on item $position",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()

            true
        }
    }
}