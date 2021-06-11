package com.myapp.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class CenterAdapter(private val centerList: List<CenterRVmodel>) :
    RecyclerView.Adapter<CenterAdapter.CenterViewHolder>() {
    class CenterViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val centernamee:TextView=itemView.findViewById(R.id.idcentername)

        val centeraddresss:TextView=itemView.findViewById(R.id.idcenterlocation)
        val centertimingg:TextView=itemView.findViewById(R.id.idcentertiming)
        val vaccinenamee:TextView=itemView.findViewById(R.id.idvaccinename)
        val vaccinefeee:TextView=itemView.findViewById(R.id.idvaccinefee)
        val agelimitt:TextView=itemView.findViewById(R.id.idagelimit)
        val availablityy:TextView=itemView.findViewById(R.id.idavailability)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {

        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.center_item,parent,false)
        return CenterViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return centerList.size

    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val center=centerList[position]
        holder.centernamee.text=center.centerName
        holder.centeraddresss.text=center.centeraddress

        holder.centertimingg.text=("From:"+center.centerFromtime+"To:"+center.centerTotime)
        holder.centertimingg.text=center.centerTotime
        holder.vaccinenamee.text= center.vaccinename.toString()
        holder.vaccinefeee.text= center.fee_type.toString()
        holder.agelimitt.text=("Age limit :"+center.agelimit.toString())
        holder.availablityy.text=("Availability :"+center.availablecapacity.toString())


    }
}